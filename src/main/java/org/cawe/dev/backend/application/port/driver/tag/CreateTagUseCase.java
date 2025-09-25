package org.cawe.dev.backend.application.port.driver.tag;

import org.cawe.dev.backend.domain.model.Tag;

public interface CreateTagUseCase {

    Tag execute(Tag tag);
}
