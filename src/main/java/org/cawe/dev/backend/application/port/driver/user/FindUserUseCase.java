package org.cawe.dev.backend.application.port.driver.user;

import org.cawe.dev.backend.domain.model.User;

public interface FindUserUseCase {

    User execute(Integer id);
}