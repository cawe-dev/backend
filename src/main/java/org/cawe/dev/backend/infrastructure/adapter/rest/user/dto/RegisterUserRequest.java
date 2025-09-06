package org.cawe.dev.backend.infrastructure.adapter.rest.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotEmpty(message = "Name cannot be empty")
        @Size(max = 100)
        String name,

        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String email
) {
}
