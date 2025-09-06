package org.cawe.dev.backend.application.port.driver;

import org.cawe.dev.backend.domain.model.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface LoginUseCase {

    User execute(Jwt jwt);
}
