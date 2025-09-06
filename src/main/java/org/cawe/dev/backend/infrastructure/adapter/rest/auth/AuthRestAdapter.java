package org.cawe.dev.backend.infrastructure.adapter.rest.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cawe.dev.backend.application.port.driver.LoginUseCase;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.rest.auth.dto.LoginUserResponse;
import org.cawe.dev.backend.infrastructure.adapter.rest.auth.mapper.AuthRestMapper;
import org.cawe.dev.backend.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestAdapter {

    private final LoginUseCase loginUseCase;
    private final AuthRestMapper authRestMapper;

    @Value("${app.name}")
    private String applicationName;

    @GetMapping("/me")
    public ResponseEntity<LoginUserResponse> getAuthenticatedUser(@AuthenticationPrincipal Jwt jwt) {
        log.debug("GET /me request for subject: {}", jwt.getSubject());
        User user = loginUseCase.execute(jwt);
        LoginUserResponse response = authRestMapper.toLoginUserResponse(user);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createLoginSuccessAlert(applicationName, response.email()))
                .body(response);
    }
}
