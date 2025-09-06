package org.cawe.dev.backend.infrastructure.adapter.rest.auth.mapper;

import org.cawe.dev.backend.application.port.driver.input.LoginUserInput;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.rest.auth.dto.LoginUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.oauth2.jwt.Jwt;

@Mapper(componentModel = "spring")
public interface AuthRestMapper {

    @Mapping(source = "subject", target = "cognitoId")
    @Mapping(target = "email", expression = "java(jwt.getClaimAsString(\"email\"))")
    @Mapping(target = "name", expression = "java(jwt.getClaimAsString(\"name\"))")
    @Mapping(target = "avatarUrl", expression = "java(jwt.getClaimAsString(\"picture\"))")
    LoginUserInput toLoginUserInput(Jwt jwt);

    LoginUserResponse toLoginUserResponse(User user);

}
