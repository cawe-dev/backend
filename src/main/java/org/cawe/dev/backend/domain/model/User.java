package org.cawe.dev.backend.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.cawe.dev.backend.domain.enumeration.RoleEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Integer id;

    @NotNull(message = "The name must be informed")
    private String name;

    @NotNull(message = "The email must be informed")
    private String email;

    @NotNull(message = "The role must be informed")
    private RoleEnum role;

    @NotNull(message = "The cognitoId must be informed")
    private String cognitoId;

    @NotNull(message = "The avatarUrl must be informed")
    private String avatarUrl;
}
