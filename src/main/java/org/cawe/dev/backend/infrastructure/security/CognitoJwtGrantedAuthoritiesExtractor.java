package org.cawe.dev.backend.infrastructure.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("cognito")
public class CognitoJwtGrantedAuthoritiesExtractor implements JwtGrantedAuthoritiesExtractor {

    private static final String COGNITO_GROUPS_CLAIM = "cognito:groups";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> extract(Jwt jwt) {
        List<String> groups = jwt.getClaim(COGNITO_GROUPS_CLAIM);

        if (groups == null || groups.isEmpty()) {
            return List.of(new SimpleGrantedAuthority(ROLE_PREFIX + "READER"));
        }

        return groups.stream()
                .map(role -> ROLE_PREFIX + role.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
