package org.cawe.dev.backend.infrastructure.adapter.persistence;

import jakarta.transaction.Transactional;
import org.cawe.dev.backend.AbstractIntegrationTest;
import org.cawe.dev.backend.domain.enumeration.RoleEnum;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.cognito.CognitoIdentityAdapter;
import org.cawe.dev.backend.infrastructure.adapter.persistence.repository.UserRepository;
import org.cawe.dev.backend.infrastructure.security.JwtGrantedAuthoritiesExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class UserAdapterIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private CognitoIdentityAdapter cognitoIdentityAdapter;

    @MockitoBean
    private JwtGrantedAuthoritiesExtractor jwtGrantedAuthoritiesExtractor;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    @DisplayName("It should save a user and persist it in the database")
    void testSaveUserAndVerifyPersistence() {
        User userToSave = User.builder()
                .name("Integration User")
                .email("integration@test.com")
                .role(RoleEnum.READER)
                .cognitoId("cognito-id-123")
                .avatarUrl("avatar.png")
                .build();

        User savedUser = userAdapter.save(userToSave);

        assertNotNull(savedUser.getId());
        assertEquals("Integration User", savedUser.getName());

        Optional<User> foundUser = userRepository.findById(savedUser.getId())
                .map(entity -> User.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .email(entity.getEmail())
                        .build());

        assertTrue(foundUser.isPresent());
        assertEquals("Integration User", foundUser.get().getName());
    }

    @Test
    @DisplayName("It should find a user by ID from the database")
    void testFindByIdWhenUserIsPersisted() {
        User userToSaveAndFind = User.builder()
                .name("FindMe User")
                .email("findme@test.com")
                .role(RoleEnum.READER)
                .cognitoId("cognito-id-find")
                .avatarUrl("avatar-find.png")
                .build();
        User persistedUser = userAdapter.save(userToSaveAndFind);


        Optional<User> foundUserOpt = userAdapter.findById(persistedUser.getId());

        assertTrue(foundUserOpt.isPresent());
        assertEquals(persistedUser.getId(), foundUserOpt.get().getId());
        assertEquals("FindMe User", foundUserOpt.get().getName());
    }

    @Test
    @DisplayName("It should update a user's data in the database")
    void testUpdateUserAndVerifyChanges() {
        User originalUser = User.builder()
                .name("Original Name")
                .email("original@test.com")
                .role(RoleEnum.READER)
                .cognitoId("cognito-id-update")
                .avatarUrl("avatar-original.png")
                .build();
        User persistedUser = userAdapter.save(originalUser);

        User updateData = User.builder()
                .name("Updated Name")
                .avatarUrl("avatar-updated.png")
                .build();

        userAdapter.update(persistedUser, updateData);

        Optional<User> updatedUserOpt = userAdapter.findById(persistedUser.getId());
        assertTrue(updatedUserOpt.isPresent());
        assertEquals("Updated Name", updatedUserOpt.get().getName());
        assertEquals("avatar-updated.png", updatedUserOpt.get().getAvatarUrl());
        assertEquals("original@test.com", updatedUserOpt.get().getEmail());
    }

    @Test
    @DisplayName("It should delete a user from the database")
    void testDeleteUserAndVerifyAbsence() {
        User userToDelete = User.builder()
                .name("ToDelete")
                .email("delete@test.com")
                .role(RoleEnum.READER)
                .cognitoId("cognito-id-delete")
                .avatarUrl("avatar-delete.png")
                .build();
        User persistedUser = userAdapter.save(userToDelete);

        assertTrue(userRepository.existsById(persistedUser.getId()));

        userAdapter.deleteById(persistedUser.getId());

        assertFalse(userRepository.existsById(persistedUser.getId()));
    }
}
