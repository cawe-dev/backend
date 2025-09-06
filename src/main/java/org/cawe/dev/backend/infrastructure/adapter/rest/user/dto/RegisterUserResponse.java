package org.cawe.dev.backend.infrastructure.adapter.rest.user.dto;

public record RegisterUserResponse(
        Integer id,
        String name,
        String email
) {
}
