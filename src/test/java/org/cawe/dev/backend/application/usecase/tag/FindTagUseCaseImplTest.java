package org.cawe.dev.backend.application.usecase.tag;

import org.cawe.dev.backend.application.port.driven.tag.FindTagById;
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
class FindTagUseCaseImplTest {

    @InjectMocks
    private FindTagUseCaseImpl findTagUseCase;

    @Mock
    private FindTagById findTagById;

    private static final int TAG_ID = 1;
    private static final String ENTITY_NAME = "Tag";
    private static final int NON_EXISTENT_TAG_ID = 999;

    @Test
    @DisplayName("It should return a tag when the id exists")
    void testFindTagByIdSuccessfully() {
        Tag tag = new Tag(TAG_ID, "Java");
        when(findTagById.findById(TAG_ID)).thenReturn(Optional.of(tag));

        Optional<Tag> result = Optional.ofNullable(findTagUseCase.execute(TAG_ID));

        assertTrue(result.isPresent());
        assertEquals(TAG_ID, result.get().getId());
        assertEquals("Java", result.get().getName());

        verify(findTagById, times(1)).findById(TAG_ID);
    }


    @Test
    @DisplayName("It should make sure to return 404 when the user does not exist")
    void testReturnNotFoundErrorWhenFindByIdWithNonExistentUser() {
        when(findTagById.findById(NON_EXISTENT_TAG_ID))
                .thenThrow(new EntityNotFoundException(ENTITY_NAME));

        assertThrows(EntityNotFoundException.class, () -> {
            findTagUseCase.execute(NON_EXISTENT_TAG_ID);
        });

        verify(findTagById, times(1)).findById(NON_EXISTENT_TAG_ID);
    }
}
