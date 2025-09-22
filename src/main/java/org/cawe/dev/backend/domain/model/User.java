package org.cawe.dev.backend.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cawe.dev.backend.domain.enumeration.RoleEnum;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel {
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
