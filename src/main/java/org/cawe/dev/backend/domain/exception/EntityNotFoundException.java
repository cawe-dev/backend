package org.cawe.dev.backend.domain.exception;

import org.cawe.dev.backend.infrastructure.adapter.rest.exception.NotFoundAlertException;

@SuppressWarnings("java:S110")
public class EntityNotFoundException extends NotFoundAlertException {

    public EntityNotFoundException(String entity) {
        super(entity + " not found.", entity, "entity-not-found");
    }
}
