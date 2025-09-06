package org.cawe.dev.backend.infrastructure.adapter.persistence.repository;

import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
