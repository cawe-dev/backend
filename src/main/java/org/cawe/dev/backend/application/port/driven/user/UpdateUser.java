package org.cawe.dev.backend.application.port.driven.user;

import org.cawe.dev.backend.domain.model.User;

public interface UpdateUser {

    User update(User userToUpdate, User user);
}
