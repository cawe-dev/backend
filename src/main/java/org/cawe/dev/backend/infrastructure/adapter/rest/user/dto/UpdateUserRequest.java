package org.cawe.dev.backend.infrastructure.adapter.rest.user.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max = 100)
        String name,
        String avatarUrl
) {
}
