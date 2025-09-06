package org.cawe.dev.backend.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CognitoJwtGrantedAuthoritiesExtractorTest {

    private static final String COGNITO_GROUPS_CLAIM = "cognito:groups";

    @Mock
    private Jwt mockJwt;

    private CognitoJwtGrantedAuthoritiesExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new CognitoJwtGrantedAuthoritiesExtractor();
    }

    @Test
    @DisplayName("It should return default READER role when cognito:groups claim is null")
    void testExtractWhenGroupsClaimIsNull() {
        when(mockJwt.getClaim(COGNITO_GROUPS_CLAIM)).thenReturn(null);

        Collection<GrantedAuthority> authorities = extractor.extract(mockJwt);

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_READER", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("It should return default READER role when cognito:groups claim is empty")
    void testExtractWhenGroupsClaimIsEmpty() {
        when(mockJwt.getClaim(COGNITO_GROUPS_CLAIM)).thenReturn(Collections.emptyList());

        Collection<GrantedAuthority> authorities = extractor.extract(mockJwt);

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_READER", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("It should extract and format a single role from cognito:groups claim")
    void testExtractWhenGroupsClaimHasSingleRole() {
        when(mockJwt.getClaim(COGNITO_GROUPS_CLAIM)).thenReturn(List.of("ADMIN"));

        Collection<GrantedAuthority> authorities = extractor.extract(mockJwt);

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("It should extract and format multiple roles from cognito:groups claim")
    void testExtractWhenGroupsClaimHasMultipleRoles() {
        when(mockJwt.getClaim(COGNITO_GROUPS_CLAIM)).thenReturn(List.of("ADMIN", "EDITOR"));

        Collection<GrantedAuthority> authorities = extractor.extract(mockJwt);
        List<String> authorityStrings = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorityStrings.contains("ROLE_ADMIN"));
        assertTrue(authorityStrings.contains("ROLE_EDITOR"));
    }

    @Test
    @DisplayName("It should correctly format roles with mixed case to uppercase")
    void testExtractWhenRoleIsMixedCase() {
        when(mockJwt.getClaim(COGNITO_GROUPS_CLAIM)).thenReturn(List.of("Admin"));

        Collection<GrantedAuthority> authorities = extractor.extract(mockJwt);

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }
}
