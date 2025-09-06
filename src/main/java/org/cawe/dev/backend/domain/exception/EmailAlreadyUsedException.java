package org.cawe.dev.backend.domain.exception;

import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.UserEntity;
import org.cawe.dev.backend.infrastructure.adapter.rest.exception.ConflictAlertException;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@SuppressWarnings("java:S110")
public class EmailAlreadyUsedException extends ConflictAlertException {

    public static final String ENTITY_NAME = getBaseNameFromEntity(UserEntity.class.getName());

    public EmailAlreadyUsedException() {
        super("Email is already in use!", ENTITY_NAME, "email-exists");
    }
}
