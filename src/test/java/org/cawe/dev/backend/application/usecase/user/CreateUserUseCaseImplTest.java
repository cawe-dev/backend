package org.cawe.dev.backend.application.usecase.user;

import org.cawe.dev.backend.application.port.driven.user.CheckUserByEmail;
import org.cawe.dev.backend.application.port.driven.user.SaveUser;
import org.cawe.dev.backend.domain.exception.EmailAlreadyUsedException;
import org.cawe.dev.backend.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    @Mock
    private SaveUser saveUser;

    @Mock
    private CheckUserByEmail checkUserByEmail;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    private User userToCreate;

    @BeforeEach
    void setUp() {
        userToCreate = new User();
        userToCreate.setEmail("test@example.com");
        userToCreate.setName("Test User");
    }

    @Test
    @DisplayName("It should be able to create a user when email is unique")
    void testCreateUserSuccessfully() {
        when(checkUserByEmail.existsByEmail(userToCreate.getEmail())).thenReturn(false);
        when(saveUser.save(any(User.class))).thenReturn(userToCreate);

        User createdUser = createUserUseCase.execute(userToCreate);

        assertNotNull(createdUser);
        assertEquals(userToCreate.getEmail(), createdUser.getEmail());
        verify(checkUserByEmail, times(1)).existsByEmail(userToCreate.getEmail());
        verify(saveUser, times(1)).save(userToCreate);
    }

    @Test
    @DisplayName("It should throw EmailAlreadyUsedException when email already exists")
    void testCreateUserWithExistingEmail() {
        when(checkUserByEmail.existsByEmail(userToCreate.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class, () -> {
            createUserUseCase.execute(userToCreate);
        });

        verify(checkUserByEmail, times(1)).existsByEmail(userToCreate.getEmail());
        verify(saveUser, never()).save(any(User.class));
    }
}