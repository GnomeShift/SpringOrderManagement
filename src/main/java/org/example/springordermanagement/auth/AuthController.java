package org.example.springordermanagement.auth;

import jakarta.validation.Valid;
import org.example.springordermanagement.customer.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final BruteforceProtectionService bruteforceProtectionService;

    public AuthController(AuthenticationManager authManager, CustomerRepository customerRepository,
                          RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils,
                          BruteforceProtectionService bruteforceProtectionService) {
        this.authManager = authManager;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.bruteforceProtectionService = bruteforceProtectionService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();

        if (bruteforceProtectionService.isLocked(email)) {
            long remainingLockTime = bruteforceProtectionService.getLockTime(email);

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Too many login attempts. Try again in " + remainingLockTime + " minutes."));
        }

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            bruteforceProtectionService.resetFailedLoginAttempts(email);

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
        }
        catch (BadCredentialsException e) {
            bruteforceProtectionService.registerFailedLogin(email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid credentials!"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signUpRequest) {
        if (customerRepository.existsByEmail((signUpRequest.getEmail()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use!"));
        }

        Customer customer = new Customer();
        customer.setName(signUpRequest.getName());
        customer.setSurname(signUpRequest.getSurname());
        customer.setAge(signUpRequest.getAge());
        customer.setEmail(signUpRequest.getEmail());
        customer.setPhone(signUpRequest.getPhone());
        customer.setPassword(encoder.encode(signUpRequest.getPassword()));
        customer.setRegistrationDate(LocalDate.now());
        customer.setFailedLoginAttempt(0);
        customer.setLockTime(null);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(Roles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found."));
            roles.add(userRole);
        }
        else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role is not found."));
                    roles.add(adminRole);
                }
                else {
                    Role userRole = roleRepository.findByName(Roles.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        customer.setRoles(roles);
        customerRepository.save(customer);

        return ResponseEntity.ok(new MessageResponse("Registered!"));
    }
}
