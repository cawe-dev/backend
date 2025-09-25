package org.cawe.dev.backend.application.port.driven.tag;

import org.cawe.dev.backend.domain.model.Tag;

public interface SaveTag {

    Tag save(Tag tag);
}
