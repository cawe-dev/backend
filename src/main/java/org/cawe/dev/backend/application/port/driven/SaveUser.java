package org.cawe.dev.backend.application.port.driven;

import org.cawe.dev.backend.domain.model.User;

public interface SaveUser {

    User save(User user);
}
