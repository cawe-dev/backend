package org.cawe.dev.backend.infrastructure.adapter.rest.user.dto;

public record UpdateUserResponse(
        Integer id,
        String name,
        String email,
        String avatarUrl
) {
}
