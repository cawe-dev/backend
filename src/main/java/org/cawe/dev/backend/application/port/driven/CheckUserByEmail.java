package org.cawe.dev.backend.application.port.driven;

public interface CheckUserByEmail {

    boolean existsByEmail(String email);
}
