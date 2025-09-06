package org.cawe.dev.backend.application.mapper;

import org.cawe.dev.backend.application.port.driver.input.LoginUserInput;
import org.cawe.dev.backend.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationUserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(org.cawe.dev.backend.domain.enumeration.RoleEnum.READER)")
    @Mapping(source = "cognitoId", target = "cognitoId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    User toDomain(LoginUserInput input);
}
