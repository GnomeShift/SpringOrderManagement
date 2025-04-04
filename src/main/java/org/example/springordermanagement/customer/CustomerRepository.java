package org.example.springordermanagement.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(@Email @NotNull String email);
    Customer findByEmail(@Email @NotNull String email);

    @Transactional
    @Modifying
    @Query("UPDATE Customer c SET c.failedLoginAttempt = :failLoginAttempts WHERE c.email = :email")
    void updateFailedLoginAttempts(@Param("email") String email, @Param("failLoginAttempts") int failLoginAttempts);

    @Transactional
    @Modifying
    @Query("UPDATE Customer c SET c.lockTime = :lockTime WHERE c.email = :email")
    void lock(@Param("email") String email, @Param("lockTime") LocalDateTime lockTime);

    @Transactional
    @Modifying
    @Query("UPDATE Customer c SET c.lockTime = null, c.failedLoginAttempt = 0 WHERE c.email = :email")
    void unlock(@Param("email") String email);
}
