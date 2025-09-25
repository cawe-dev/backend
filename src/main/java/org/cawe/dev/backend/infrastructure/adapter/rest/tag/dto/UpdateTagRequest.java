package org.cawe.dev.backend.infrastructure.adapter.rest.tag.dto;

import jakarta.validation.constraints.Size;

public record UpdateTagRequest(
        @Size(max = 100)
        String name
) {
}
