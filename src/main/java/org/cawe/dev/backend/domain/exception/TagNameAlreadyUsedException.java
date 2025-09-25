package org.cawe.dev.backend.domain.exception;

import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.TagEntity;
import org.cawe.dev.backend.infrastructure.adapter.rest.exception.ConflictAlertException;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@SuppressWarnings("java:S110")
public class TagNameAlreadyUsedException extends ConflictAlertException {

    public static final String ENTITY_NAME = getBaseNameFromEntity(TagEntity.class.getName());

    public TagNameAlreadyUsedException() {
        super("Tag name is already in use!", ENTITY_NAME, "tag-name-exists");
    }
}
