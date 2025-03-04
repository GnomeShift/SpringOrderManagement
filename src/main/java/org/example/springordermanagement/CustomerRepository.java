package org.example.springordermanagement;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(@Email @NotNull String email);

    Customer findByEmail(@Email @NotNull String email);
}
