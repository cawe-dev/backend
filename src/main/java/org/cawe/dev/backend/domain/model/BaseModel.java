package org.cawe.dev.backend.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel {

    @NotNull(message = "The create at date must be informed")
    private Instant createdAt;

    @NotNull(message = "The update at date must be informed")
    private Instant updatedAt;
}
