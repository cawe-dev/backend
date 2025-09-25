package org.cawe.dev.backend.application.usecase.user;

import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.user.FindUserById;
import org.cawe.dev.backend.application.port.driver.user.FindUserUseCase;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.UserEntity;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@RequiredArgsConstructor
public class FindUserUseCaseImpl implements FindUserUseCase {

    private final FindUserById findUserById;

    public static final String ENTITY_NAME = getBaseNameFromEntity(UserEntity.class.getName());

    @Override
    public User execute(Integer id) {
        return findUserById.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME));
    }
}
