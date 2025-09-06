package org.cawe.dev.backend.infrastructure.adapter.rest.user;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cawe.dev.backend.application.port.driver.CreateUserUseCase;
import org.cawe.dev.backend.application.port.driver.DeleteUserUseCase;
import org.cawe.dev.backend.application.port.driver.FindUserUseCase;
import org.cawe.dev.backend.application.port.driver.UpdateUserUseCase;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.UserEntity;
import org.cawe.dev.backend.infrastructure.adapter.rest.user.dto.*;
import org.cawe.dev.backend.infrastructure.adapter.rest.user.mapper.UserRestMapper;
import org.cawe.dev.backend.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestAdapter {

    private final FindUserUseCase findUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserRestMapper userRestMapper;

    @Value("${app.name}")
    private String applicationName;

    public static final String ENTITY_NAME = getBaseNameFromEntity(UserEntity.class.getName());

    @GetMapping(value = "/{id}")
    @Operation(summary = "Search for a user by id")
    public ResponseEntity<DetailsUserResponse> getUser(@PathVariable @Valid final Integer id) {
        User user = this.findUserUseCase.execute(id);
        DetailsUserResponse response = this.userRestMapper.toDetailsUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a user")
    public ResponseEntity<RegisterUserResponse> createUser(@RequestBody @Valid final RegisterUserRequest registerUserRequest) throws URISyntaxException {
        log.debug("REST request to save User : {}", registerUserRequest);
        User user = this.userRestMapper.toUser(registerUserRequest);
        user = this.createUserUseCase.execute(user);

        RegisterUserResponse response = this.userRestMapper.toRegisterUserResponse(user);

        return ResponseEntity
                .created(new URI("/api/v1/users/" + response.id()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, response.id().toString()))
                .body(response);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a user")
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable @Valid final Integer id,
                                                         @RequestBody @Valid final UpdateUserRequest updateUserRequest) {
        log.debug("REST request to update User : {}", updateUserRequest);
        User user = this.userRestMapper.toUser(updateUserRequest);
        user = this.updateUserUseCase.execute(id, user);

        UpdateUserResponse response = this.userRestMapper.toUpdateUserResponse(user);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, response.id().toString()))
                .body(response);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<Object> deleteUser(@PathVariable @Valid final Integer id) {
        this.deleteUserUseCase.execute(id);

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, String.valueOf(id))).build();
    }
}
