package org.example.springordermanagement.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import org.example.springordermanagement.customer.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    @Getter
    private final Long id;

    @Email
    private final String email;

    @Getter
    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Customer customer) {
        List<GrantedAuthority> authorities = customer.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

        return new UserDetailsImpl(customer.getId(), customer.getEmail(), customer.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getUsername() {
        return email;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        UserDetailsImpl user = (UserDetailsImpl) obj;

        return Objects.equals(id, user.id);
    }
}
