package org.cawe.dev.backend.application.usecase;

import org.cawe.dev.backend.application.mapper.ApplicationUserMapper;
import org.cawe.dev.backend.application.port.driven.FetchUserIdentity;
import org.cawe.dev.backend.application.port.driven.FindUserByEmail;
import org.cawe.dev.backend.application.port.driver.CreateUserUseCase;
import org.cawe.dev.backend.application.port.driver.input.LoginUserInput;
import org.cawe.dev.backend.domain.exception.UserIdentityNotFoundException;
import org.cawe.dev.backend.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private FindUserByEmail findUserByEmail;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private ApplicationUserMapper applicationUserMapper;

    @Mock
    private FetchUserIdentity fetchUserIdentity;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    private Jwt mockJwt;
    private LoginUserInput externalUser;
    private User userDomain;

    @BeforeEach
    void setUp() {
        mockJwt = new Jwt("mock-token", Instant.now(), Instant.now().plusSeconds(3600), Map.of("alg", "none"), Map.of("sub", "123"));
        externalUser = new LoginUserInput("cognito123", "Test User", "test@example.com", "avatar.png");

        userDomain = User.builder()
                .id(1)
                .cognitoId("cognito123")
                .name("Test User")
                .email("test@example.com")
                .avatarUrl("avatar.png")
                .build();
    }

    @Test
    @DisplayName("It should return an existing user if their email is already in the database")
    void testExecuteWhenUserExistsShouldReturnExistingUser() {
        when(fetchUserIdentity.findByJwt(any(Jwt.class))).thenReturn(Optional.of(externalUser));
        when(findUserByEmail.findByEmail(externalUser.email())).thenReturn(Optional.of(userDomain));

        User result = loginUseCase.execute(mockJwt);

        assertNotNull(result);
        assertEquals(userDomain.getId(), result.getId());
        assertEquals(userDomain.getEmail(), result.getEmail());

        verify(fetchUserIdentity, times(1)).findByJwt(any(Jwt.class));
        verify(findUserByEmail, times(1)).findByEmail(externalUser.email());
        verify(createUserUseCase, never()).execute(any(User.class));
        verify(applicationUserMapper, never()).toDomain(any(LoginUserInput.class));
    }

    @Test
    @DisplayName("It should create and return a new user if their email is not in the database")
    void testExecuteWhenUserDoesNotExistShouldCreateAndReturnNewUser() {
        User newUserToCreate = User.builder().email("test@example.com").name("Test User").build();
        User createdUser = User.builder().id(2).email("test@example.com").name("Test User").build();

        when(fetchUserIdentity.findByJwt(any(Jwt.class))).thenReturn(Optional.of(externalUser));
        when(findUserByEmail.findByEmail(externalUser.email())).thenReturn(Optional.empty());
        when(applicationUserMapper.toDomain(externalUser)).thenReturn(newUserToCreate);
        when(createUserUseCase.execute(newUserToCreate)).thenReturn(createdUser);

        User result = loginUseCase.execute(mockJwt);

        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals(createdUser.getEmail(), result.getEmail());

        verify(fetchUserIdentity, times(1)).findByJwt(any(Jwt.class));
        verify(findUserByEmail, times(1)).findByEmail(externalUser.email());
        verify(applicationUserMapper, times(1)).toDomain(externalUser);
        verify(createUserUseCase, times(1)).execute(newUserToCreate);
    }

    @Test
    @DisplayName("It should throw UserIdentityNotFoundException when user info cannot be fetched from the external client")
    void testExecuteWhenExternalUserFetchFailsShouldThrowException() {
        when(fetchUserIdentity.findByJwt(any(Jwt.class))).thenReturn(Optional.empty());

        UserIdentityNotFoundException exception = assertThrows(UserIdentityNotFoundException.class, () -> {
            loginUseCase.execute(mockJwt);
        });

        assertEquals("Could not retrieve user info from external identity provider.", exception.getMessage());

        verify(fetchUserIdentity, times(1)).findByJwt(any(Jwt.class));
        verify(findUserByEmail, never()).findByEmail(anyString());
        verify(createUserUseCase, never()).execute(any(User.class));
    }
}

