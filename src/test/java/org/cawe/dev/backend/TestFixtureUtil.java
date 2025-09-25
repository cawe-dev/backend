package org.cawe.dev.backend;


import jakarta.transaction.Transactional;
import org.cawe.dev.backend.domain.enumeration.RoleEnum;
import org.cawe.dev.backend.domain.model.Tag;
import org.cawe.dev.backend.domain.model.User;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.UserEntity;
import org.cawe.dev.backend.infrastructure.adapter.persistence.mapper.UserPersistenceMapper;
import org.cawe.dev.backend.infrastructure.adapter.persistence.repository.UserRepository;
import org.cawe.dev.backend.infrastructure.adapter.rest.tag.dto.RegisterTagRequest;
import org.cawe.dev.backend.infrastructure.adapter.rest.user.dto.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class TestFixtureUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPersistenceMapper userPersistenceMapper;

    private static final Random RANDOM = new SecureRandom();

    @Transactional
    public UserEntity createUserEntity() {
        String random = this.getRandom();
        UserEntity userEntity = new UserEntity();
        userEntity.setName(String.format("Name%s", random));
        userEntity.setEmail(String.format("generic.user%s@email.com", random));
        userEntity.setAvatarUrl("http://avatar.png");

        return userRepository.save(userEntity);
    }

    @Transactional
    public User createUser() {
        String random = this.getRandom();
        return User.builder()
                .id(Integer.valueOf(random))
                .cognitoId(String.format("cognito%s", random))
                .name(String.format("Name%s", random))
                .email(String.format("generic.user%s@email.com", random))
                .role(RoleEnum.READER)
                .avatarUrl("http://avatar.png")
                .build();
    }

    public RegisterUserRequest createRegisterUserRequest(User user) {
        return new RegisterUserRequest(user.getName(), user.getEmail());
    }

    public Tag createTag() {
        String random = this.getRandom();

        return Tag.builder()
                .id(Integer.valueOf(random))
                .name(String.format("TagName%s", random))
                .build();
    }


    public RegisterTagRequest createRegisterTagRequest(Tag tag) {
        return new RegisterTagRequest(tag.getName());
    }

    private String getRandom() {
        return String.valueOf(RANDOM.nextInt(10000000));
    }

}
