package org.cawe.dev.backend.application.usecase.user;

import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.port.driven.user.CheckUserByEmail;
import org.cawe.dev.backend.application.port.driven.user.SaveUser;
import org.cawe.dev.backend.application.port.driver.user.CreateUserUseCase;
import org.cawe.dev.backend.domain.exception.EmailAlreadyUsedException;
import org.cawe.dev.backend.domain.model.User;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final SaveUser saveUser;
    private final CheckUserByEmail checkUserByEmail;

    @Override
    @Transactional
    public User execute(User userToCreate) {
        if (checkUserByEmail.existsByEmail(userToCreate.getEmail())) {
            throw new EmailAlreadyUsedException();
        }

        return saveUser.save(userToCreate);
    }
}
