package org.cawe.dev.backend.infrastructure.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuditAwareTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt mockJwt;

    private CustomAuditAware customAuditAware;

    @BeforeEach
    void setUp() {
        customAuditAware = new CustomAuditAware();
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("It should return the JWT subject when the principal is a JWT")
    void testGetCurrentAuditorWhenPrincipalIsJwt() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(mockJwt);
        when(mockJwt.getSubject()).thenReturn("user-jwt-subject");

        Optional<String> currentAuditor = customAuditAware.getCurrentAuditor();

        assertTrue(currentAuditor.isPresent());
        assertEquals("user-jwt-subject", currentAuditor.get());
    }

    @Test
    @DisplayName("It should return the principal's string representation for non-JWT principals")
    void testGetCurrentAuditorWhenPrincipalIsNotJwt() {
        String stringPrincipal = "user-string-principal";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(stringPrincipal);

        Optional<String> currentAuditor = customAuditAware.getCurrentAuditor();

        assertTrue(currentAuditor.isPresent());
        assertEquals("user-string-principal", currentAuditor.get());
    }

    @Test
    @DisplayName("It should return an empty optional when user is not authenticated")
    void testGetCurrentAuditorWhenNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        Optional<String> currentAuditor = customAuditAware.getCurrentAuditor();

        assertTrue(currentAuditor.isEmpty());
    }

    @Test
    @DisplayName("It should return an empty optional when there is no authentication")
    void testGetCurrentAuditorWhenNoAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        Optional<String> currentAuditor = customAuditAware.getCurrentAuditor();

        assertTrue(currentAuditor.isEmpty());
    }
}
