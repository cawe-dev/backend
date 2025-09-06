package org.cawe.dev.backend.infrastructure.adapter.rest.user.mapper;

import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.rest.user.dto.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

    User toUser(RegisterUserRequest registerUserRequest);

    User toUser(UpdateUserRequest updateUserRequest);

    RegisterUserRequest toRegisterUserRequest(User player);

    RegisterUserResponse toRegisterUserResponse(User player);

    UpdateUserResponse toUpdateUserResponse(User player);

    DetailsUserResponse toDetailsUserResponse(User player);
}
