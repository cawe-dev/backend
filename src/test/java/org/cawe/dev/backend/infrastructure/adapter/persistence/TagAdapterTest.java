package org.cawe.dev.backend.infrastructure.adapter.persistence;

import org.cawe.dev.backend.domain.model.Tag;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.TagEntity;
import org.cawe.dev.backend.infrastructure.adapter.persistence.mapper.TagPersistenceMapper;
import org.cawe.dev.backend.infrastructure.adapter.persistence.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagAdapterTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagPersistenceMapper tagPersistenceMapper;

    @InjectMocks
    private TagAdapter tagAdapter;

    private Tag tagDomain;
    private TagEntity tagEntity;
    private static final Integer TAG_ID = 1;

    @BeforeEach
    void setUp() {
        tagDomain = new Tag(1, "Java");

        tagEntity = new TagEntity();
        tagEntity.setId(1);
        tagEntity.setName("Java");
    }

    @Test
    @DisplayName("It should find a tag by ID when tag exists")
    void testFindByIdWhenTagExists() {
        when(tagRepository.findById(TAG_ID)).thenReturn(Optional.of(tagEntity));
        when(tagPersistenceMapper.toDomain(tagEntity)).thenReturn(tagDomain);

        Optional<Tag> result = tagAdapter.findById(TAG_ID);

        assertTrue(result.isPresent());
        assertEquals(tagDomain, result.get());
        verify(tagRepository, times(1)).findById(TAG_ID);
        verify(tagPersistenceMapper, times(1)).toDomain(tagEntity);
    }

    @Test
    @DisplayName("It should return an empty optional when tag does not exist by ID")
    void testFindByIdWhenTagDoesNotExist() {
        when(tagRepository.findById(TAG_ID)).thenReturn(Optional.empty());

        Optional<Tag> result = tagAdapter.findById(TAG_ID);

        assertTrue(result.isEmpty());
        verify(tagRepository, times(1)).findById(TAG_ID);
        verify(tagPersistenceMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("It should find all tags successfully")
    void testFindAllTagsSuccessfully() {
        Pageable pageable = Pageable.unpaged();
        Page<TagEntity> pageEntity = new PageImpl<>(List.of(tagEntity));
        when(tagRepository.findAll(pageable)).thenReturn(pageEntity);
        when(tagPersistenceMapper.toDomain(tagEntity)).thenReturn(tagDomain);

        Page<Tag> result = tagAdapter.findAll(pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(tagDomain, result.getContent().get(0));
        verify(tagRepository, times(1)).findAll(pageable);
        verify(tagPersistenceMapper, times(1)).toDomain(tagEntity);
    }

    @Test
    @DisplayName("It should return an empty page when no tags exist")
    void testFindAllTagsWhenEmpty() {
        Pageable pageable = Pageable.unpaged();
        Page<TagEntity> emptyPage = new PageImpl<>(Collections.emptyList());
        when(tagRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<Tag> result = tagAdapter.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tagRepository, times(1)).findAll(pageable);
        verify(tagPersistenceMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("It should save a tag and return the saved tag")
    void testSaveTag() {
        when(tagPersistenceMapper.toEntity(tagDomain)).thenReturn(tagEntity);
        when(tagRepository.save(tagEntity)).thenReturn(tagEntity);
        when(tagPersistenceMapper.toDomain(tagEntity)).thenReturn(tagDomain);

        Tag result = tagAdapter.save(tagDomain);

        assertNotNull(result);
        assertEquals(tagDomain, result);
        verify(tagPersistenceMapper, times(1)).toEntity(tagDomain);
        verify(tagRepository, times(1)).save(tagEntity);
        verify(tagPersistenceMapper, times(1)).toDomain(tagEntity);
    }

    @Test
    @DisplayName("It should update a tag and return the updated tag")
    void testUpdateTag() {
        Tag tagWithUpdateData = new Tag(null, "Updated Name");
        Tag updatedTagDomain = new Tag(1, "Updated Name");

        when(tagPersistenceMapper.toEntity(tagDomain)).thenReturn(tagEntity);
        doNothing().when(tagPersistenceMapper).toUpdateEntity(tagWithUpdateData, tagEntity);
        when(tagRepository.save(tagEntity)).thenReturn(tagEntity);
        when(tagPersistenceMapper.toDomain(tagEntity)).thenReturn(updatedTagDomain);

        Tag result = tagAdapter.update(tagDomain, tagWithUpdateData);

        assertNotNull(result);
        assertEquals(updatedTagDomain, result);
        verify(tagPersistenceMapper, times(1)).toEntity(tagDomain);
        verify(tagPersistenceMapper, times(1)).toUpdateEntity(tagWithUpdateData, tagEntity);
        verify(tagRepository, times(1)).save(tagEntity);
        verify(tagPersistenceMapper, times(1)).toDomain(tagEntity);
    }

    @Test
    @DisplayName("It should delete a tag by ID")
    void testDeleteTagById() {
        when(tagRepository.findById(TAG_ID)).thenReturn(Optional.of(tagEntity));
        when(tagPersistenceMapper.toDomain(tagEntity)).thenReturn(tagDomain);
        when(tagPersistenceMapper.toEntity(tagDomain)).thenReturn(tagEntity);
        doNothing().when(tagRepository).delete(tagEntity);

        tagAdapter.deleteById(TAG_ID);

        verify(tagRepository, times(1)).delete(tagEntity);
    }

    @Test
    @DisplayName("It should return true when checking for an existing name")
    void testExistsByNameReturnsTrue() {
        String name = "Java";
        when(tagRepository.existsByName(name)).thenReturn(true);

        boolean result = tagAdapter.existsByName(name);

        assertTrue(result);
        verify(tagRepository, times(1)).existsByName(name);
    }

    @Test
    @DisplayName("It should return true when checking for an existing ID")
    void testExistsByIdReturnsTrue() {
        when(tagRepository.existsById(TAG_ID)).thenReturn(true);

        boolean result = tagAdapter.existsById(TAG_ID);

        assertTrue(result);
        verify(tagRepository, times(1)).existsById(TAG_ID);
    }
}

