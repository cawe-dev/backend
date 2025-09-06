package org.cawe.dev.backend.application.port.driven;

import org.cawe.dev.backend.domain.model.User;

import java.util.Optional;

public interface FindUserById {

    Optional<User> findById(Integer id);
}
