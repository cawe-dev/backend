package org.cawe.dev.backend.application.port.driven.tag;

import org.cawe.dev.backend.domain.model.Tag;

import java.util.Optional;

public interface FindTagById {

    Optional<Tag> findById(Integer id);
}
