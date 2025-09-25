package org.cawe.dev.backend.application.port.driver.tag;

import org.cawe.dev.backend.domain.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllTagsUseCase {

    Page<Tag> findAll(Pageable pageable);
}
