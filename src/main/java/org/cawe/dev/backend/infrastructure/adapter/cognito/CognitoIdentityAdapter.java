package org.cawe.dev.backend.infrastructure.adapter.cognito;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cawe.dev.backend.application.port.driven.user.FetchUserIdentity;
import org.cawe.dev.backend.application.port.driver.input.LoginUserInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CognitoIdentityAdapter implements FetchUserIdentity {

    private final WebClient.Builder webClientBuilder;

    private static final String EMAIL_ATTRIBUTE = "email";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String SUB_ATTRIBUTE = "sub";
    private static final String PICTURE_ATTRIBUTE = "picture";


    @Value("${aws.cognito.user-info-uri}")
    private String userInfoEndpoint;

    @Override
    public Optional<LoginUserInput> findByJwt(Jwt jwt) {
        log.debug("Fetching user info from Cognito for subject: {}", jwt.getSubject());

        try {
            Map userAttributes = webClientBuilder.build()
                    .get()
                    .uri(this.userInfoEndpoint)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (userAttributes == null || userAttributes.get(EMAIL_ATTRIBUTE) == null) {
                log.error("Could not fetch user attributes or email is missing.");
                return Optional.empty();
            }

            String name = (String) userAttributes.get(NAME_ATTRIBUTE);
            if (name == null || name.trim().isEmpty()) {
                String email = (String) userAttributes.get(EMAIL_ATTRIBUTE);
                name = email.split("@")[0];
            }

            LoginUserInput userInfo = new LoginUserInput(
                    (String) userAttributes.get(SUB_ATTRIBUTE),
                    (String) userAttributes.get(EMAIL_ATTRIBUTE),
                    name,
                    (String) userAttributes.get(PICTURE_ATTRIBUTE)
            );

            return Optional.of(userInfo);

        } catch (Exception e) {
            log.error("Error calling Cognito UserInfo endpoint", e);
            return Optional.empty();
        }
    }
}