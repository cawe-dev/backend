package org.cawe.dev.backend.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudienceValidatorTest {

    private static final String APP_CLIENT_ID = "test-client-id";

    @Mock
    private Jwt mockJwt;

    private AudienceValidator audienceValidator;

    @BeforeEach
    void setUp() {
        audienceValidator = new AudienceValidator(APP_CLIENT_ID);
    }

    @Test
    @DisplayName("It should throw an exception when created with a null client ID")
    void testConstructorWithNullClientId() {
        assertThrows(IllegalArgumentException.class, () -> new AudienceValidator(null));
    }

    @Test
    @DisplayName("It should throw an exception when created with a blank client ID")
    void testConstructorWithBlankClientId() {
        assertThrows(IllegalArgumentException.class, () -> new AudienceValidator("  "));
    }

    @Test
    @DisplayName("It should succeed when audience list contains the client ID")
    void testValidateWhenAudienceContainsClientId() {
        when(mockJwt.getAudience()).thenReturn(List.of("other-audience", APP_CLIENT_ID));

        OAuth2TokenValidatorResult result = audienceValidator.validate(mockJwt);

        assertFalse(result.hasErrors());
    }

    @Test
    @DisplayName("It should succeed when client_id claim matches the client ID")
    void testValidateWhenClientIdClaimMatches() {
        when(mockJwt.getAudience()).thenReturn(Collections.emptyList());
        when(mockJwt.getClaimAsString("client_id")).thenReturn(APP_CLIENT_ID);

        OAuth2TokenValidatorResult result = audienceValidator.validate(mockJwt);

        assertFalse(result.hasErrors());
    }

    @Test
    @DisplayName("It should fail when neither audience nor client_id claim matches")
    void testValidateWhenNoClaimMatches() {
        when(mockJwt.getAudience()).thenReturn(List.of("some-other-audience"));
        when(mockJwt.getClaimAsString("client_id")).thenReturn("some-other-client-id");

        OAuth2TokenValidatorResult result = audienceValidator.validate(mockJwt);

        assertTrue(result.hasErrors());
    }

    @Test
    @DisplayName("It should fail when audience is null and client_id does not match")
    void testValidateWhenAudienceIsNullAndClientIdFails() {
        when(mockJwt.getAudience()).thenReturn(null);
        when(mockJwt.getClaimAsString("client_id")).thenReturn("wrong-client-id");

        OAuth2TokenValidatorResult result = audienceValidator.validate(mockJwt);

        assertTrue(result.hasErrors());
    }

    @Test
    @DisplayName("It should fail when audience and client_id claims are not present")
    void testValidateWhenClaimsAreNotPresent() {
        when(mockJwt.getAudience()).thenReturn(null);
        when(mockJwt.getClaimAsString("client_id")).thenReturn(null);

        OAuth2TokenValidatorResult result = audienceValidator.validate(mockJwt);

        assertTrue(result.hasErrors());
        assertEquals("invalid_token", result.getErrors().iterator().next().getErrorCode());
    }
}
