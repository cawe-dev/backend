package org.cawe.dev.backend.infrastructure.adapter.persistence;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.tag.*;
import org.cawe.dev.backend.domain.model.Tag;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.TagEntity;
import org.cawe.dev.backend.infrastructure.adapter.persistence.mapper.TagPersistenceMapper;
import org.cawe.dev.backend.infrastructure.adapter.persistence.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
public class TagAdapter implements SaveTag, FindTagById, FindTags, CheckTagByName, CheckTagById, UpdateTag, DeleteTag {

    private final TagRepository tagRepository;

    private final TagPersistenceMapper tagPersistenceMapper;

    @Override
    @Transactional
    public Tag save(Tag tag) {
        TagEntity tagEntity = tagPersistenceMapper.toEntity(tag);
        TagEntity savedEntity = tagRepository.save(tagEntity);
        return tagPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Tag> findById(Integer id) {
        return this.tagRepository.findById(id)
                .map(this.tagPersistenceMapper::toDomain);
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable)
                .map(tagPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public Tag update(Tag tagToUpdate, Tag tag) {
        TagEntity tagEntityToUpdate = this.tagPersistenceMapper.toEntity(tagToUpdate);

        this.tagPersistenceMapper.toUpdateEntity(tag, tagEntityToUpdate);
        TagEntity updatedEntity = this.tagRepository.save(tagEntityToUpdate);

        return this.tagPersistenceMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        TagEntity tagEntity = this.tagPersistenceMapper.toEntity(
                this.findById(id).orElseThrow()
        );

        this.tagRepository.delete(tagEntity);
    }

    public boolean existsByName(String name) {
        return this.tagRepository.existsByName(name);
    }

    @Override
    public boolean existsById(Integer id) {
        return this.tagRepository.existsById(id);
    }
}
