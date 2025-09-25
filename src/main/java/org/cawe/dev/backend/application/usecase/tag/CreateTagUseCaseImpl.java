package org.cawe.dev.backend.application.usecase.tag;

import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.tag.CheckTagByName;
import org.cawe.dev.backend.application.port.driven.tag.SaveTag;
import org.cawe.dev.backend.application.port.driver.tag.CreateTagUseCase;
import org.cawe.dev.backend.domain.exception.TagNameAlreadyUsedException;
import org.cawe.dev.backend.domain.model.Tag;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CreateTagUseCaseImpl implements CreateTagUseCase {

    private final SaveTag saveTag;
    private final CheckTagByName checkTagByName;

    @Override
    @Transactional
    public Tag execute(Tag tagToCreate) {
        if (checkTagByName.existsByName(tagToCreate.getName())) {
            throw new TagNameAlreadyUsedException();
        }

        return saveTag.save(tagToCreate);
    }
}
