package org.cawe.dev.backend.infrastructure.adapter.persistence;

import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.UserEntity;
import org.cawe.dev.backend.infrastructure.adapter.persistence.mapper.UserPersistenceMapper;
import org.cawe.dev.backend.infrastructure.adapter.persistence.repository.UserRepository;
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
class UserAdapterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @InjectMocks
    private UserAdapter userAdapter;

    private User userDomain;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userDomain = User.builder()
                .id(1)
                .name("Test User")
                .email("test@example.com")
                .build();

        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
    }

    @Test
    @DisplayName("It should find a user by ID when user exists")
    void testFindByIdWhenUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(userPersistenceMapper.toDomain(userEntity)).thenReturn(userDomain);

        Optional<User> result = userAdapter.findById(1);

        assertTrue(result.isPresent());
        assertEquals(userDomain, result.get());
        verify(userRepository, times(1)).findById(1);
        verify(userPersistenceMapper, times(1)).toDomain(userEntity);
    }

    @Test
    @DisplayName("It should return an empty optional when user does not exist by ID")
    void testFindByIdWhenUserDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Optional<User> result = userAdapter.findById(1);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findById(1);
        verify(userPersistenceMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("It should find a user by email when user exists")
    void testFindByEmailWhenUserExists() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userPersistenceMapper.toDomain(userEntity)).thenReturn(userDomain);

        Optional<User> result = userAdapter.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(userDomain, result.get());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userPersistenceMapper, times(1)).toDomain(userEntity);
    }

    @Test
    @DisplayName("It should return an empty optional when user does not exist by email")
    void testFindByEmailWhenUserDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userAdapter.findByEmail(email);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userPersistenceMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("It should save a user and return the saved user")
    void testSaveUser() {
        when(userPersistenceMapper.toEntity(userDomain)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userPersistenceMapper.toDomain(userEntity)).thenReturn(userDomain);

        User result = userAdapter.save(userDomain);

        assertNotNull(result);
        assertEquals(userDomain, result);
        verify(userPersistenceMapper, times(1)).toEntity(userDomain);
        verify(userRepository, times(1)).save(userEntity);
        verify(userPersistenceMapper, times(1)).toDomain(userEntity);
    }

    @Test
    @DisplayName("It should update a user and return the updated user")
    void testUpdateUser() {
        User userWithUpdateData = User.builder().name("Updated Name").build();
        User updatedUserDomain = User.builder().id(1).name("Updated Name").email("test@example.com").build();

        when(userPersistenceMapper.toEntity(userDomain)).thenReturn(userEntity);
        doNothing().when(userPersistenceMapper).toUpdateEntity(userWithUpdateData, userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userPersistenceMapper.toDomain(userEntity)).thenReturn(updatedUserDomain);

        User result = userAdapter.update(userDomain, userWithUpdateData);

        assertNotNull(result);
        assertEquals(updatedUserDomain, result);
        verify(userPersistenceMapper, times(1)).toEntity(userDomain);
        verify(userPersistenceMapper, times(1)).toUpdateEntity(userWithUpdateData, userEntity);
        verify(userRepository, times(1)).save(userEntity);
        verify(userPersistenceMapper, times(1)).toDomain(userEntity);
    }

    @Test
    @DisplayName("It should delete a user by ID")
    void testDeleteUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(userPersistenceMapper.toDomain(userEntity)).thenReturn(userDomain);
        when(userPersistenceMapper.toEntity(userDomain)).thenReturn(userEntity);
        doNothing().when(userRepository).delete(userEntity);

        userAdapter.deleteById(1);

        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    @DisplayName("It should return true when checking for an existing email")
    void testExistsByEmailReturnsTrue() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userAdapter.existsByEmail(email);

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("It should return true when checking for an existing ID")
    void testExistsByIdReturnsTrue() {
        Integer id = 1;
        when(userRepository.existsById(id)).thenReturn(true);

        boolean result = userAdapter.existsById(id);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(id);
    }
}
