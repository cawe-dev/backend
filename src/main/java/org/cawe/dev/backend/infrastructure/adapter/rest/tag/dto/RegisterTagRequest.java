package org.cawe.dev.backend.infrastructure.adapter.rest.tag.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterTagRequest(
        @NotEmpty(message = "Name cannot be empty")
        @Size(max = 100)
        String name
) {
}
