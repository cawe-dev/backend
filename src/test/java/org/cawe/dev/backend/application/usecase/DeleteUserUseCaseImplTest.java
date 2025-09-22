package org.cawe.dev.backend.application.usecase;

import org.cawe.dev.backend.application.port.driven.user.CheckUserById;
import org.cawe.dev.backend.application.port.driven.user.DeleteUser;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

    @Mock
    private DeleteUser deleteUser;

    @Mock
    private CheckUserById checkUserById;

    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    private static final int USER_ID = 1;

    @Test
    @DisplayName("It should be able to delete a user when user exists")
    void testDeleteUserSuccessfully() {
        when(checkUserById.existsById(USER_ID)).thenReturn(true);
        doNothing().when(deleteUser).deleteById(USER_ID);

        deleteUserUseCase.execute(USER_ID);

        verify(checkUserById, times(1)).existsById(USER_ID);
        verify(deleteUser, times(1)).deleteById(USER_ID);
    }

    @Test
    @DisplayName("It should throw EntityNotFoundException when the user does not exist")
    void testDeleteUserNotFound() {
        when(checkUserById.existsById(USER_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            deleteUserUseCase.execute(USER_ID);
        });

        verify(checkUserById, times(1)).existsById(USER_ID);
        verify(deleteUser, never()).deleteById(anyInt());
    }
}