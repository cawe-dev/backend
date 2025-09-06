package org.cawe.dev.backend.infrastructure.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

import java.util.List;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final String appClientId;

    public AudienceValidator(String appClientId) {
        Assert.hasText(appClientId, "appClientId cannot be empty");
        this.appClientId = appClientId;
    }

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        List<String> audience = jwt.getAudience();
        if (audience != null && audience.contains(this.appClientId)) {
            return OAuth2TokenValidatorResult.success();
        }

        String clientIdClaim = jwt.getClaimAsString("client_id");
        if (this.appClientId.equals(clientIdClaim)) {
            return OAuth2TokenValidatorResult.success();
        }

        OAuth2Error error = new OAuth2Error("invalid_token", "The token does not have the expected audience (client ID).", null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
