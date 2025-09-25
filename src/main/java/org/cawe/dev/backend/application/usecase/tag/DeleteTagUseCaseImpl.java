package org.cawe.dev.backend.application.usecase.tag;

import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.tag.CheckTagById;
import org.cawe.dev.backend.application.port.driven.tag.DeleteTag;
import org.cawe.dev.backend.application.port.driver.tag.DeleteTagUseCase;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.TagEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@RequiredArgsConstructor
public class DeleteTagUseCaseImpl implements DeleteTagUseCase {

    private final DeleteTag deleteTag;
    private final CheckTagById checkTagById;

    public static final String ENTITY_NAME = getBaseNameFromEntity(TagEntity.class.getName());

    @Override
    @Transactional
    public void execute(Integer id) {
        if (!checkTagById.existsById(id)) {
            throw new EntityNotFoundException(ENTITY_NAME);
        }

        deleteTag.deleteById(id);
    }
}
