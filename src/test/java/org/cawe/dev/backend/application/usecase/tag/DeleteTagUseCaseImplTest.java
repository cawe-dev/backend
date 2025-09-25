package org.cawe.dev.backend.application.usecase.tag;

import org.cawe.dev.backend.application.port.driven.tag.CheckTagById;
import org.cawe.dev.backend.application.port.driven.tag.DeleteTag;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTagUseCaseImplTest {

    @InjectMocks
    private DeleteTagUseCaseImpl deleteTagUseCase;

    @Mock
    private DeleteTag deleteTag;

    @Mock
    private CheckTagById checkTagById;

    private static final int TAG_ID = 1;

    @Test
    @DisplayName("It should be able to delete a tag when tag exists")
    void testDeleteTagSuccessfully() {
        when(checkTagById.existsById(TAG_ID)).thenReturn(true);
        doNothing().when(deleteTag).deleteById(TAG_ID);

        assertDoesNotThrow(() -> deleteTagUseCase.execute(TAG_ID));

        verify(checkTagById, times(1)).existsById(TAG_ID);
        verify(deleteTag, times(1)).deleteById(TAG_ID);
    }

    @Test
    @DisplayName("It should throw EntityNotFoundException when the tag does not exist")
    void testDeleteTagNotFound() {
        when(checkTagById.existsById(TAG_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> deleteTagUseCase.execute(TAG_ID));

        verify(checkTagById, times(1)).existsById(TAG_ID);
        verify(deleteTag, never()).deleteById(TAG_ID);
    }
}

