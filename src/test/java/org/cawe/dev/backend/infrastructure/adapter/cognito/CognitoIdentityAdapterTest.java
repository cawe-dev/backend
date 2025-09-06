package org.cawe.dev.backend.infrastructure.adapter.cognito;

import org.cawe.dev.backend.application.port.driver.input.LoginUserInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CognitoIdentityAdapterTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CognitoIdentityAdapter cognitoIdentityAdapter;

    private Jwt mockJwt;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(cognitoIdentityAdapter, "userInfoEndpoint", "http://fake-cognito-url.com/userInfo");

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(any(String.class), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        mockJwt = new Jwt("mock-token", Instant.now(), Instant.now().plusSeconds(3600), Map.of("alg", "none"), Map.of("sub", "cognito-user-id"));
    }

    @Test
    @DisplayName("It should return user info when API call is successful")
    void testFindByJwtWhenApiCallIsSuccessful() {
        Map<String, String> cognitoResponse = Map.of(
                "sub", "cognito-user-id",
                "email", "test@example.com",
                "name", "Test User",
                "picture", "http://example.com/avatar.png"
        );
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(cognitoResponse));

        Optional<LoginUserInput> result = cognitoIdentityAdapter.findByJwt(mockJwt);

        assertTrue(result.isPresent());
        LoginUserInput userInfo = result.get();
        assertEquals("cognito-user-id", userInfo.cognitoId());
        assertEquals("Test User", userInfo.name());
        assertEquals("test@example.com", userInfo.email());
        assertEquals("http://example.com/avatar.png", userInfo.avatarUrl());
    }

    @Test
    @DisplayName("It should derive name from email when name attribute is missing")
    void testFindByJwtWhenNameIsMissingShouldDeriveFromName() {
        Map<String, String> cognitoResponse = Map.of(
                "sub", "cognito-user-id",
                "email", "user.part@example.com",
                "picture", "http://example.com/avatar.png"
        );
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(cognitoResponse));

        Optional<LoginUserInput> result = cognitoIdentityAdapter.findByJwt(mockJwt);

        assertTrue(result.isPresent());
        assertEquals("user.part", result.get().name());
    }

    @Test
    @DisplayName("It should return empty when API response is missing email")
    void testFindByJwtWhenEmailIsMissingShouldReturnEmpty() {
        Map<String, String> cognitoResponse = Map.of(
                "sub", "cognito-user-id",
                "name", "Test User"
        );
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(cognitoResponse));

        Optional<LoginUserInput> result = cognitoIdentityAdapter.findByJwt(mockJwt);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("It should return empty when API call fails")
    void testFindByJwtWhenApiCallFailsShouldReturnEmpty() {
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        Optional<LoginUserInput> result = cognitoIdentityAdapter.findByJwt(mockJwt);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("It should return empty when API returns an empty body")
    void testFindByJwtWhenApiReturnsEmptyBodyShouldReturnEmpty() {
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.empty());

        Optional<LoginUserInput> result = cognitoIdentityAdapter.findByJwt(mockJwt);

        assertTrue(result.isEmpty());
    }
}
