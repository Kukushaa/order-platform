package com.kukusha.users_shared_lib.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(@NotBlank(message = "Name is required") String name,
                                 @NotBlank(message = "Surname is required") String surname,
                                 @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
                                 @NotBlank(message = "Username is required") @Size(min = 3, message = "Username must be at least 3 characters") String username,
                                 @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password) {
}
