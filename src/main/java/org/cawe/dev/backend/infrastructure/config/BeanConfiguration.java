package org.cawe.dev.backend.infrastructure.config;

import org.cawe.dev.backend.application.mapper.ApplicationUserMapper;
import org.cawe.dev.backend.application.port.driven.tag.*;
import org.cawe.dev.backend.application.port.driven.user.*;
import org.cawe.dev.backend.application.port.driver.tag.*;
import org.cawe.dev.backend.application.port.driver.user.*;
import org.cawe.dev.backend.application.usecase.tag.*;
import org.cawe.dev.backend.application.usecase.user.*;
import org.cawe.dev.backend.infrastructure.adapter.persistence.TagAdapter;
import org.cawe.dev.backend.infrastructure.adapter.persistence.UserAdapter;
import org.cawe.dev.backend.infrastructure.adapter.persistence.mapper.TagPersistenceMapper;
import org.cawe.dev.backend.infrastructure.adapter.persistence.mapper.UserPersistenceMapper;
import org.cawe.dev.backend.infrastructure.adapter.persistence.repository.TagRepository;
import org.cawe.dev.backend.infrastructure.adapter.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public UserAdapter userAdapter(final UserRepository userRepository, final UserPersistenceMapper userPersistenceMapper) {
        return new UserAdapter(userRepository, userPersistenceMapper);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(final SaveUser saveUser, final CheckUserByEmail checkUserByEmail) {
        return new CreateUserUseCaseImpl(saveUser, checkUserByEmail);
    }

    @Bean
    public FindUserUseCase findUserUseCase(final FindUserById findUserById) {
        return new FindUserUseCaseImpl(findUserById);
    }

    @Bean
    public FindUserByEmail findUserByEmail(UserAdapter userAdapter) {
        return userAdapter::findByEmail;
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(final UpdateUser updateUser, final FindUserById findUserById) {
        return new UpdateUserUseCaseImpl(updateUser, findUserById);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(final DeleteUser deleteUser, final CheckUserById checkUserById) {
        return new DeleteUserUseCaseImpl(deleteUser, checkUserById);
    }

    @Bean
    public CheckUserByEmail checkUserByEmail(UserAdapter userAdapter) {
        return userAdapter::existsByEmail;
    }

    @Bean
    public CheckUserById checkUserById(UserAdapter userAdapter) {
        return userAdapter::existsById;
    }

    @Bean
    public LoginUseCase loginUseCase(final FindUserByEmail findUserByEmail, final CreateUserUseCase createUserUseCase, final ApplicationUserMapper applicationUserMapper, final FetchUserIdentity fetchUserIdentity) {
        return new LoginUseCaseImpl(findUserByEmail, createUserUseCase, applicationUserMapper, fetchUserIdentity);
    }

    @Bean
    public TagAdapter tagAdapter(final TagRepository tagRepository, final TagPersistenceMapper tagPersistenceMapper) {
        return new TagAdapter(tagRepository, tagPersistenceMapper);
    }

    @Bean
    public CreateTagUseCase createTagUseCase(final SaveTag saveTag, final CheckTagByName checkTagByName) {
        return new CreateTagUseCaseImpl(saveTag, checkTagByName);
    }

    @Bean
    public FindTagUseCase findTagUseCase(final FindTagById findTagById) {
        return new FindTagUseCaseImpl(findTagById);
    }

    @Bean
    public FindAllTagsUseCase findAllTagsUseCase(final FindTags findTags) {
        return new FindAllTagsUseCaseImpl(findTags);
    }

    @Bean
    public UpdateTagUseCase updateTagUseCase(final UpdateTag updateTag, final FindTagById findTagById) {
        return new UpdateTagUseCaseImpl(updateTag, findTagById);
    }

    @Bean
    public DeleteTagUseCase deleteTagUseCase(final DeleteTag deleteTag, final CheckTagById checkTagById) {
        return new DeleteTagUseCaseImpl(deleteTag, checkTagById);
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
