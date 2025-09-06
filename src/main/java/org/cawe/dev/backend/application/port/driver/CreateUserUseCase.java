package org.cawe.dev.backend.application.port.driver;

import org.cawe.dev.backend.domain.model.User;

public interface CreateUserUseCase {

    User execute(User user);
}
