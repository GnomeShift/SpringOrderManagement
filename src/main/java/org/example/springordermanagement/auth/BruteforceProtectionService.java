package org.example.springordermanagement.auth;

import jakarta.validation.constraints.Email;
import org.example.springordermanagement.customer.Customer;
import org.example.springordermanagement.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class BruteforceProtectionService {
    private final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    private final long LOCK_TIME = TimeUnit.MINUTES.toMinutes(1);

    @Autowired
    private CustomerRepository customerRepository;

    public void registerFailedLogin(@Email String email) {
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            return;
        }

        int failedLoginAttempts = customer.getFailedLoginAttempt() + 1;
        customerRepository.updateFailedLoginAttempts(email, failedLoginAttempts);

        if (failedLoginAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
            lockUser(email);
        }
    }

    public void resetFailedLoginAttempts(@Email String email) {
        customerRepository.unlock(email);
    }

    public boolean isLocked(@Email String email) {
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            return false;
        }

        LocalDateTime lockTime = customer.getLockTime();
        return lockTime != null && lockTime.isAfter(LocalDateTime.now());
    }

    public void lockUser(@Email String email) {
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            return;
        }

        LocalDateTime lockUntil = LocalDateTime.now().plusMinutes(LOCK_TIME);
        customerRepository.lock(email, lockUntil);
    }

    public long getLockTime(@Email String email) {
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            return 0;
        }

        LocalDateTime lockTime = customer.getLockTime();

        if (lockTime != null && lockTime.isAfter(LocalDateTime.now())) {
            return Duration.between(LocalDateTime.now(), lockTime).toMinutes();
        }

        return 0;
    }
}
