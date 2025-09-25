package org.cawe.dev.backend.application.usecase.tag;

import org.cawe.dev.backend.application.port.driven.tag.CheckTagByName;
import org.cawe.dev.backend.application.port.driven.tag.SaveTag;
import org.cawe.dev.backend.domain.model.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTagUseCaseImplTest {

    @InjectMocks
    private CreateTagUseCaseImpl createTagUseCase;

    @Mock
    private SaveTag saveTag;

    @Mock
    private CheckTagByName checkTagByName;

    @Test
    @DisplayName("It should create a tag successfully when name is not in use")
    void testCreateTagSuccessfully() {
        Tag tagToSave = new Tag(null, "Java");
        Tag savedTag = new Tag(1, "Java");

        when(checkTagByName.existsByName("Java")).thenReturn(false);
        when(saveTag.save(tagToSave)).thenReturn(savedTag);

        Tag result = createTagUseCase.execute(tagToSave);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Java", result.getName());

        verify(checkTagByName, times(1)).existsByName("Java");
        verify(saveTag, times(1)).save(tagToSave);
    }

    @Test
    @DisplayName("It should throw RuntimeException when the tag name already exists")
    void testCreateTagWithExistingName() {
        Tag tagToSave = new Tag(null, "Java");
        when(checkTagByName.existsByName("Java")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> createTagUseCase.execute(tagToSave));

        verify(checkTagByName, times(1)).existsByName("Java");
        verify(saveTag, never()).save(any(Tag.class));
    }
}
