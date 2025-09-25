package org.cawe.dev.backend.infrastructure.adapter.persistence;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.user.*;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.UserEntity;
import org.cawe.dev.backend.infrastructure.adapter.persistence.mapper.UserPersistenceMapper;
import org.cawe.dev.backend.infrastructure.adapter.persistence.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserAdapter implements SaveUser, FindUserById, FindUserByEmail, CheckUserByEmail, CheckUserById, UpdateUser, DeleteUser {

    private final UserRepository userRepository;

    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    @Transactional
    public User save(User user) {
        UserEntity userEntity = userPersistenceMapper.toEntity(user);
        UserEntity savedEntity = userRepository.save(userEntity);
        return userPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return this.userRepository.findById(id)
                .map(this.userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .map(this.userPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public User update(User userToUpdate, User user) {
        UserEntity userEntityToUpdate = this.userPersistenceMapper.toEntity(userToUpdate);

        this.userPersistenceMapper.toUpdateEntity(user, userEntityToUpdate);
        UserEntity updatedEntity = this.userRepository.save(userEntityToUpdate);

        return this.userPersistenceMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        UserEntity userEntity = this.userPersistenceMapper.toEntity(
                this.findById(id).orElseThrow()
        );

        this.userRepository.delete(userEntity);
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public boolean existsById(Integer id) {
        return this.userRepository.existsById(id);
    }
}
