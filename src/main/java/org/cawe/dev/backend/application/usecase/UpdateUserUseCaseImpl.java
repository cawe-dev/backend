package org.cawe.dev.backend.application.usecase;

import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.FindUserById;
import org.cawe.dev.backend.application.port.driven.UpdateUser;
import org.cawe.dev.backend.application.port.driver.UpdateUserUseCase;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UpdateUser updateUser;
    private final FindUserById findUserById;

    public static final String ENTITY_NAME = getBaseNameFromEntity(UserEntity.class.getName());

    @Override
    @Transactional
    public User execute(Integer id, User user) {
        return findUserById.findById(id)
                .map(userToUpdate -> updateUser.update(userToUpdate, user))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME));
    }
}
