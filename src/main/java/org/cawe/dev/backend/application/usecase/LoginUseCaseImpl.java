package org.cawe.dev.backend.application.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cawe.dev.backend.application.mapper.ApplicationUserMapper;
import org.cawe.dev.backend.application.port.driven.FetchUserIdentity;
import org.cawe.dev.backend.application.port.driven.FindUserByEmail;
import org.cawe.dev.backend.application.port.driver.CreateUserUseCase;
import org.cawe.dev.backend.application.port.driver.LoginUseCase;
import org.cawe.dev.backend.domain.exception.UserIdentityNotFoundException;
import org.cawe.dev.backend.domain.model.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {

    private final FindUserByEmail findUserByEmail;
    private final CreateUserUseCase createUserUseCase;
    private final ApplicationUserMapper applicationUserMapper;
    private final FetchUserIdentity fetchUserIdentity;

    @Override
    @Transactional
    public User execute(Jwt jwt) {
        var externalUser = fetchUserIdentity.findByJwt(jwt)
                .orElseThrow(UserIdentityNotFoundException::new);

        Optional<User> userOptional = findUserByEmail.findByEmail(externalUser.email());
        return userOptional.orElseGet(() -> createUserUseCase.execute(this.applicationUserMapper.toDomain(externalUser)));
    }
}
