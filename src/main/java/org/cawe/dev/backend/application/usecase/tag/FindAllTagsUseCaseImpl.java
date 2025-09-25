package org.cawe.dev.backend.application.usecase.tag;

import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.tag.FindTags;
import org.cawe.dev.backend.application.port.driver.tag.FindAllTagsUseCase;
import org.cawe.dev.backend.domain.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FindAllTagsUseCaseImpl implements FindAllTagsUseCase {

    private final FindTags findTags;

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return findTags.findAll(pageable);
    }
}
