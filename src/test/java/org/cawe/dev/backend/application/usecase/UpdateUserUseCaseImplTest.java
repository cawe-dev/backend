package org.cawe.dev.backend.application.usecase;

import org.cawe.dev.backend.application.port.driven.FindUserById;
import org.cawe.dev.backend.application.port.driven.UpdateUser;
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
class UpdateUserUseCaseImplTest {

    @Mock
    private UpdateUser updateUser;

    @Mock
    private FindUserById findUserById;

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    private User existingUser;
    private User userWithUpdates;
    private static final Integer NON_EXISTENT_USER_ID = 1;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(1);
        existingUser.setName("Old Name");
        existingUser.setEmail("test@example.com");

        userWithUpdates = new User();
        userWithUpdates.setName("New Name");
    }

    @Test
    @DisplayName("It should be able to update a user when user exists")
    void testUpdateUserSuccessfully() {
        when(findUserById.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        when(updateUser.update(any(User.class), any(User.class))).thenAnswer(invocation -> {
            User userToUpdate = invocation.getArgument(0);
            User updates = invocation.getArgument(1);
            userToUpdate.setName(updates.getName());
            return userToUpdate;
        });

        User updatedUser = updateUserUseCase.execute(existingUser.getId(), userWithUpdates);

        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName(), "The user's name should have been updated.");
        assertEquals(existingUser.getEmail(), updatedUser.getEmail(), "The user's email must remain the same.");

        verify(findUserById, times(1)).findById(existingUser.getId());
        verify(updateUser, times(1)).update(existingUser, userWithUpdates);
    }

    @Test
    @DisplayName("It should throw EntityNotFoundException when the user does not exist")
    void testUpdateUserNotFound() {
        when(findUserById.findById(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            updateUserUseCase.execute(NON_EXISTENT_USER_ID, userWithUpdates);
        });

        verify(findUserById, times(1)).findById(NON_EXISTENT_USER_ID);
        verify(updateUser, never()).update(any(User.class), any(User.class));
    }
}