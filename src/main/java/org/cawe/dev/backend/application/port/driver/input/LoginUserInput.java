package org.cawe.dev.backend.application.port.driver.input;

public record LoginUserInput(
        String cognitoId,
        String email,
        String name,
        String avatarUrl
) {
}
