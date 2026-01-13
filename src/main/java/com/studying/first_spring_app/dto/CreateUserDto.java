package com.studying.first_spring_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank(message = "Username is required")
        @Size(max = 100)
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 5, message = "Password must be at least 5 characters")
        @Size(max = 50, message = "Password is too large")
        String password
) {
}
