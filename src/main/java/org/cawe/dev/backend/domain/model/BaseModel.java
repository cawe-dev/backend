package org.cawe.dev.backend.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel {

    @NotNull(message = "The create at date must be informed")
    private Instant createdAt;

    @NotNull(message = "The update at date must be informed")
    private Instant updatedAt;
}
