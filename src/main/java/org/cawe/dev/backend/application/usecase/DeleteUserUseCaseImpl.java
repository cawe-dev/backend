package org.cawe.dev.backend.application.usecase;

import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.CheckUserById;
import org.cawe.dev.backend.application.port.driven.DeleteUser;
import org.cawe.dev.backend.application.port.driver.DeleteUserUseCase;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final DeleteUser deleteUser;
    private final CheckUserById checkUserById;

    public static final String ENTITY_NAME = getBaseNameFromEntity(UserEntity.class.getName());

    @Override
    @Transactional
    public void execute(Integer id) {
        if (!checkUserById.existsById(id)) {
            throw new EntityNotFoundException(ENTITY_NAME);
        }

        deleteUser.deleteById(id);
    }
}
