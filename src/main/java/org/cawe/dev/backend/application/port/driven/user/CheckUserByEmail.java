package org.cawe.dev.backend.application.port.driven.user;

public interface CheckUserByEmail {

    boolean existsByEmail(String email);
}
