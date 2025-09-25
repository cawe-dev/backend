package org.cawe.dev.backend.application.port.driven.tag;

import org.cawe.dev.backend.domain.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindTags {

    Page<Tag> findAll(Pageable pageable);

}
