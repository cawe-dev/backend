package org.cawe.dev.backend.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

@FunctionalInterface
public interface JwtGrantedAuthoritiesExtractor {
    Collection<GrantedAuthority> extract(Jwt jwt);
}
