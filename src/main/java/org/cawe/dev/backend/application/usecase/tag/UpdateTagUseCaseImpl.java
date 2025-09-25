package org.cawe.dev.backend.application.usecase.tag;

import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.tag.FindTagById;
import org.cawe.dev.backend.application.port.driven.tag.UpdateTag;
import org.cawe.dev.backend.application.port.driver.tag.UpdateTagUseCase;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.domain.model.Tag;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.TagEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@RequiredArgsConstructor
public class UpdateTagUseCaseImpl implements UpdateTagUseCase {
    private final UpdateTag updateTag;
    private final FindTagById findTagById;

    public static final String ENTITY_NAME = getBaseNameFromEntity(TagEntity.class.getName());

    @Override
    @Transactional
    public Tag execute(Integer id, Tag tag) {
        return findTagById.findById(id)
                .map(tagToUpdate -> updateTag.update(tagToUpdate, tag))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME));
    }
}
