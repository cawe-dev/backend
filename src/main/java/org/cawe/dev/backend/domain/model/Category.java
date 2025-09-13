package org.cawe.dev.backend.domain.model;

import jakarta.validation.constraints.NotNull;

public class Category extends BaseModel {
    private Integer id;

    @NotNull(message = "The name date must be informed")
    private String name;
}
