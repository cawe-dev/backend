package org.cawe.dev.backend.infrastructure.adapter.persistence.mapper;

import org.cawe.dev.backend.domain.model.Tag;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.TagEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TagPersistenceMapper {

    Tag toDomain(TagEntity tag);

    TagEntity toEntity(Tag tag);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateEntity(Tag domain, @MappingTarget TagEntity entity);
}
