package org.cawe.dev.backend.application.port.driven;

import org.cawe.dev.backend.domain.model.User;

public interface UpdateUser {

    User update(User userToUpdate, User user);
}
