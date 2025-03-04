package org.example.springordermanagement.auth;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @Size(min = 2)
    @NotNull
    private String name;
    @Size(min = 2)
    @NotNull
    private String surname;

    @Min(18)
    @Max(99)
    private int age;

    @Email
    @NotNull
    private String email;

    @Pattern(regexp = "^\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$")
    private String phone;

    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*()_+{}\\[\\]:;<>,.?~`\"'|\\\\/-]{6,}$")
    @NotNull
    private String password;

    private Set<String> role;
}
