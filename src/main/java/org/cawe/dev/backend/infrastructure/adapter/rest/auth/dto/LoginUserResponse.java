package org.cawe.dev.backend.infrastructure.adapter.rest.auth.dto;

import org.cawe.dev.backend.domain.enumeration.RoleEnum;

public record LoginUserResponse(
        Integer id,
        String name,
        String email,
        RoleEnum role
) {
}
