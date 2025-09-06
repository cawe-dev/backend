package org.cawe.dev.backend.infrastructure.adapter.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cawe.dev.backend.AbstractIntegrationTest;
import org.cawe.dev.backend.TestFixtureUtil;
import org.cawe.dev.backend.application.port.driver.CreateUserUseCase;
import org.cawe.dev.backend.application.port.driver.DeleteUserUseCase;
import org.cawe.dev.backend.application.port.driver.FindUserUseCase;
import org.cawe.dev.backend.application.port.driver.UpdateUserUseCase;
import org.cawe.dev.backend.domain.exception.EmailAlreadyUsedException;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.cognito.CognitoIdentityAdapter;
import org.cawe.dev.backend.infrastructure.adapter.rest.user.dto.RegisterUserRequest;
import org.cawe.dev.backend.infrastructure.adapter.rest.user.dto.UpdateUserRequest;
import org.cawe.dev.backend.infrastructure.security.JwtGrantedAuthoritiesExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class UserRestAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestFixtureUtil testFixtureUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private FindUserUseCase findUserUseCase;

    @MockitoBean
    private UpdateUserUseCase updateUserUseCase;

    @MockitoBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockitoBean
    private CognitoIdentityAdapter cognitoIdentityAdapter;

    @MockitoBean
    private JwtGrantedAuthoritiesExtractor jwtGrantedAuthoritiesExtractor;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    public static final String BASE_URL = "/api/v1/users";
    private static final String JSON_PATH_ID = "$.id";
    private static final String JSON_PATH_NAME = "$.name";
    private static final String JSON_PATH_EMAIL = "$.email";
    private static final String ENTITY_NAME = "User";
    private static final Integer NON_EXISTENT_USER_ID = 999;

    @Test
    @WithMockUser
    @DisplayName("It should be able to create a new user successfully")
    void testCreateUserSuccessfully() throws Exception {
        User user = this.testFixtureUtil.createUser();
        RegisterUserRequest requestBody = this.testFixtureUtil.createRegisterUserRequest(user);

        when(createUserUseCase.execute(any(User.class))).thenReturn(user);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BASE_URL.concat("/" + user.getId())))
                .andExpect(jsonPath(JSON_PATH_ID, is(user.getId())))
                .andExpect(jsonPath(JSON_PATH_NAME, is(user.getName())));

        verify(createUserUseCase, times(1)).execute(any(User.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should return 400 (Bad Request) when trying to create user with blank name")
    void testReturnBadRequestWhenCreatingUserWithBlankName() throws Exception {
        RegisterUserRequest invalidRequestBody = new RegisterUserRequest("", "test@email.com");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("It should throw EmailAlreadyUsedException when email already exists")
    void testCreateUserWithExistingEmail() throws Exception {
        User user = this.testFixtureUtil.createUser();
        RegisterUserRequest requestBody = this.testFixtureUtil.createRegisterUserRequest(user);

        when(createUserUseCase.execute(any(User.class))).thenThrow(new EmailAlreadyUsedException());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isConflict());

        verify(createUserUseCase, times(1)).execute(any(User.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should find and return a user by ID")
    void testFindUserById() throws Exception {
        User user = this.testFixtureUtil.createUser();

        when(findUserUseCase.execute(user.getId())).thenReturn(user);

        mockMvc.perform(get(BASE_URL + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID, is(user.getId())))
                .andExpect(jsonPath(JSON_PATH_NAME, is(user.getName())))
                .andExpect(jsonPath(JSON_PATH_EMAIL, is(user.getEmail())));

        verify(findUserUseCase, times(1)).execute(user.getId());
    }

    @Test
    @WithMockUser
    @DisplayName("It should return 404 (Not Found) when fetching a non-existent user")
    void testReturnNotFoundWhenFindingNonExistentUser() throws Exception {
        when(findUserUseCase.execute(NON_EXISTENT_USER_ID)).thenThrow(new EntityNotFoundException(ENTITY_NAME));

        mockMvc.perform(get(BASE_URL + "/" + NON_EXISTENT_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(findUserUseCase, times(1)).execute(NON_EXISTENT_USER_ID);
    }

    @Test
    @WithMockUser
    @DisplayName("It should update a user successfully")
    void testUpdateUserSuccessfully() throws Exception {
        User existingUser = testFixtureUtil.createUser();
        UpdateUserRequest updateRequest = new UpdateUserRequest("Updated Name", "new-avatar.png");

        User updatedUser = User.builder()
                .id(existingUser.getId())
                .name(updateRequest.name())
                .email(existingUser.getEmail())
                .avatarUrl(updateRequest.avatarUrl())
                .build();

        when(updateUserUseCase.execute(eq(existingUser.getId()), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put(BASE_URL + "/" + existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID, is(updatedUser.getId())))
                .andExpect(jsonPath(JSON_PATH_NAME, is(updatedUser.getName())));

        verify(updateUserUseCase, times(1)).execute(eq(existingUser.getId()), any(User.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should return 404 (Not Found) when trying to update a non-existent user")
    void testReturnNotFoundWhenUpdatingNonExistentUser() throws Exception {
        UpdateUserRequest updateRequest = new UpdateUserRequest("Any Name", "any-avatar.png");

        when(updateUserUseCase.execute(eq(NON_EXISTENT_USER_ID), any(User.class))).thenThrow(new EntityNotFoundException(ENTITY_NAME));

        mockMvc.perform(put(BASE_URL + "/" + NON_EXISTENT_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(updateUserUseCase, times(1)).execute(eq(NON_EXISTENT_USER_ID), any(User.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should delete a user successfully")
    void testDeleteUserSuccessfully() throws Exception {
        Integer userId = 1;
        doNothing().when(deleteUserUseCase).execute(userId);

        mockMvc.perform(delete(BASE_URL + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deleteUserUseCase, times(1)).execute(userId);
    }

    @Test
    @WithMockUser
    @DisplayName("It should return 404 (Not Found) when trying to delete a non-existent user")
    void testReturnNotFoundWhenDeletingNonExistentUser() throws Exception {
        doThrow(new EntityNotFoundException(ENTITY_NAME)).when(deleteUserUseCase).execute(NON_EXISTENT_USER_ID);

        mockMvc.perform(delete(BASE_URL + "/" + NON_EXISTENT_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deleteUserUseCase, times(1)).execute(NON_EXISTENT_USER_ID);
    }
}

