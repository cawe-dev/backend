package org.cawe.dev.backend.application.port.driven;

import org.cawe.dev.backend.application.port.driver.input.LoginUserInput;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface FetchUserIdentity {
    Optional<LoginUserInput> findByJwt(Jwt jwt);
}