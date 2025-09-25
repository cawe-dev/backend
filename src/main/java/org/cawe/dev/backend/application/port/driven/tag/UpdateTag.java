package org.cawe.dev.backend.application.port.driven.tag;

import org.cawe.dev.backend.domain.model.Tag;

public interface UpdateTag {

    Tag update(Tag tagToUpdate, Tag tag);
}
