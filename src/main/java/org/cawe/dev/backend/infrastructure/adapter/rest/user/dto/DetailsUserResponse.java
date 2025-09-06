package org.cawe.dev.backend.infrastructure.adapter.rest.user.dto;

public record DetailsUserResponse(
        Integer id,
        String name,
        String email,
        String role,
        String cognitoId,
        String avatarUrl
) {
}
