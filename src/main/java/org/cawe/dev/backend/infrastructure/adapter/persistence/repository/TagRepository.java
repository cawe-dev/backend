package org.cawe.dev.backend.infrastructure.adapter.persistence.repository;

import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    boolean existsByName(String name);

    Optional<TagEntity> findByName(String name);

}
