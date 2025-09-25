package org.cawe.dev.backend.application.usecase.tag;

import org.cawe.dev.backend.application.port.driven.tag.FindTags;
import org.cawe.dev.backend.domain.model.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllTagsUseCaseImplTest {

    @InjectMocks
    private FindAllTagsUseCaseImpl findAllTagsUseCase;

    @Mock
    private FindTags findTags;

    @Test
    @DisplayName("It should return a page of tags")
    void testFindAllTagsSuccessfully() {
        int tagId = 1;
        Tag tag = new Tag(tagId, "Java");
        Pageable pageable = Pageable.unpaged();
        Page<Tag> page = new PageImpl<>(List.of(tag));

        when(findTags.findAll(pageable)).thenReturn(page);

        Page<Tag> result = findAllTagsUseCase.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Java", result.getContent().getFirst().getName());

        verify(findTags, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("It should return an empty page when no tags exist")
    void testFindAllTagsWhenEmpty() {
        Pageable pageable = Pageable.unpaged();
        Page<Tag> emptyPage = new PageImpl<>(Collections.emptyList());

        when(findTags.findAll(pageable)).thenReturn(emptyPage);

        Page<Tag> result = findAllTagsUseCase.findAll(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());

        verify(findTags, times(1)).findAll(pageable);
    }
}

