package org.cawe.dev.backend.infrastructure.adapter.rest.home;

import org.cawe.dev.backend.AbstractIntegrationTest;
import org.cawe.dev.backend.infrastructure.adapter.cognito.CognitoIdentityAdapter;
import org.cawe.dev.backend.infrastructure.security.JwtGrantedAuthoritiesExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class HomeControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CognitoIdentityAdapter cognitoIdentityAdapter;

    @MockitoBean
    private JwtGrantedAuthoritiesExtractor jwtGrantedAuthoritiesExtractor;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    @DisplayName("It should redirect from root path to Swagger UI")
    void testRedirectToSwaggerShouldReturnRedirect() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/swagger-ui/index.html"));
    }

    @Test
    @DisplayName("It should return the ReDoc HTML page")
    void testGetRedocShouldReturnHtmlContent() throws Exception {
        mockMvc.perform(get("/redoc"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("<title>ReDoc API Documentation</title>")))
                .andExpect(content().string(containsString("<redoc spec-url='/v3/api-docs'></redoc>")));
    }
}
