package org.cawe.dev.backend.application.usecase.tag;

import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.tag.FindTagById;
import org.cawe.dev.backend.application.port.driver.tag.FindTagUseCase;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.domain.model.Tag;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.TagEntity;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@RequiredArgsConstructor
public class FindTagUseCaseImpl implements FindTagUseCase {

    private final FindTagById findTagById;

    public static final String ENTITY_NAME = getBaseNameFromEntity(TagEntity.class.getName());

    @Override
    public Tag execute(Integer id) {
        return findTagById.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME));
    }
}
