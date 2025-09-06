package org.cawe.dev.backend;

import org.cawe.dev.backend.infrastructure.adapter.cognito.CognitoIdentityAdapter;
import org.cawe.dev.backend.infrastructure.security.JwtGrantedAuthoritiesExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@SuppressWarnings("unused")
class BackendApplicationTests extends AbstractIntegrationTest {

    @MockitoBean
    private CognitoIdentityAdapter cognitoIdentityAdapter;

    @MockitoBean
    private JwtGrantedAuthoritiesExtractor jwtGrantedAuthoritiesExtractor;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void contextLoads() {
        //Application Test Context Loader
    }

}
