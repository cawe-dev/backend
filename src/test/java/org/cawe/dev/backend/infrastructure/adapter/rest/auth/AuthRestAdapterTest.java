package org.cawe.dev.backend.infrastructure.adapter.rest.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cawe.dev.backend.AbstractIntegrationTest;
import org.cawe.dev.backend.TestFixtureUtil;
import org.cawe.dev.backend.application.port.driver.user.LoginUseCase;
import org.cawe.dev.backend.domain.exception.UserIdentityNotFoundException;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.cognito.CognitoIdentityAdapter;
import org.cawe.dev.backend.infrastructure.security.JwtGrantedAuthoritiesExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuthRestAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestFixtureUtil testFixtureUtil;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private CognitoIdentityAdapter cognitoIdentityAdapter;

    @MockitoBean
    private JwtGrantedAuthoritiesExtractor jwtGrantedAuthoritiesExtractor;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    private static final String BASE_URL = "/api/v1/auth";

    @Test
    @DisplayName("It should return authenticated user data on successful login")
    void testGetAuthenticatedUserSuccessfully() throws Exception {
        User user = this.testFixtureUtil.createUser();

        when(loginUseCase.execute(any(Jwt.class))).thenReturn(user);

        mockMvc.perform(get(BASE_URL + "/me")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.role", is(user.getRole().toString())));
    }

    @Test
    @DisplayName("It should return 401 Unauthorized when user identity cannot be fetched")
    void testGetAuthenticatedUserFailsWhenIdentityIsNotFound() throws Exception {
        when(loginUseCase.execute(any(Jwt.class))).thenThrow(new UserIdentityNotFoundException());

        mockMvc.perform(get(BASE_URL + "/me")
                        .with(jwt()))
                .andExpect(status().isUnauthorized());
    }
}
