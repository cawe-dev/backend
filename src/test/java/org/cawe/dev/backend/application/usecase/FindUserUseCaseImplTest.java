package org.cawe.dev.backend.application.usecase;

import org.cawe.dev.backend.application.port.driven.FindUserById;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserUseCaseImplTest {

    @Mock
    private FindUserById findUserById;

    @InjectMocks
    private FindUserUseCaseImpl findUserUseCase;

    private User userToFind;
    private User expectedUser;
    private static final String ENTITY_NAME = "User";
    private static final int NON_EXISTENT_USER_ID = 99999;

    @BeforeEach
    void setUp() {
        userToFind = User.builder()
                .id(1)
                .name("Generic PLayer")
                .email("generic.user@new.com")
                .build();

        expectedUser = User.builder()
                .id(1)
                .name("Generic PLayer")
                .email("generic.user@new.com")
                .build();
    }

    @Test
    @DisplayName("It should be able to return user if exists")
    void testFindById() {
        when(findUserById.findById(userToFind.getId())).thenReturn(Optional.ofNullable(expectedUser));

        User userFound = findUserUseCase.execute(userToFind.getId());

        assertNotNull(userFound.getId());
        assertEquals(userToFind.getId(), userFound.getId());

        verify(findUserById, times(1)).findById(userToFind.getId());
    }

    @Test
    @DisplayName("It should make sure to return 404 when the user does not exist")
    void testReturnNotFoundErrorWhenFindByIdWithNonExistentUser() {
        when(findUserById.findById(NON_EXISTENT_USER_ID))
                .thenThrow(new EntityNotFoundException(ENTITY_NAME));

        assertThrows(EntityNotFoundException.class, () -> {
            findUserUseCase.execute(NON_EXISTENT_USER_ID);
        });

        verify(findUserById, times(1)).findById(NON_EXISTENT_USER_ID);
    }

}