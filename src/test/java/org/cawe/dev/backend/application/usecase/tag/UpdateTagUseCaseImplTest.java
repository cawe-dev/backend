package org.cawe.dev.backend.application.usecase.tag;

import org.cawe.dev.backend.application.port.driven.tag.FindTagById;
import org.cawe.dev.backend.application.port.driven.tag.UpdateTag;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.domain.model.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTagUseCaseImplTest {

    @InjectMocks
    private UpdateTagUseCaseImpl updateTagUseCase;

    @Mock
    private UpdateTag updateTag;

    @Mock
    private FindTagById findTagById;

    private static final int TAG_ID = 1;

    @Test
    @DisplayName("It should be able to update a tag when tag exists")
    void testUpdateTagSuccessfully() {
        Tag existingTag = new Tag(TAG_ID, "Java");
        Tag updatedTagInput = new Tag(TAG_ID, "Java 17");
        Tag updatedTagOutput = new Tag(TAG_ID, "Java 17");

        when(findTagById.findById(TAG_ID)).thenReturn(Optional.of(existingTag));
        when(updateTag.update(any(Tag.class), any(Tag.class))).thenReturn(updatedTagOutput);

        Tag result = updateTagUseCase.execute(TAG_ID, updatedTagInput);

        assertNotNull(result);
        assertEquals(TAG_ID, result.getId());
        assertEquals("Java 17", result.getName());

        verify(findTagById, times(1)).findById(TAG_ID);
        verify(updateTag, times(1)).update(any(Tag.class), any(Tag.class));
    }

    @Test
    @DisplayName("It should throw EntityNotFoundException when the tag to update does not exist")
    void testUpdateTagNotFound() {
        Tag tagToUpdate = new Tag(TAG_ID, "Java 17");

        when(findTagById.findById(TAG_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> updateTagUseCase.execute(TAG_ID, tagToUpdate));

        verify(findTagById, times(1)).findById(TAG_ID);
        verify(updateTag, never()).update(any(Tag.class), any(Tag.class));
    }
}

