package org.cawe.dev.backend.infrastructure.adapter.rest.exception;

import org.zalando.problem.Status;

@SuppressWarnings("java:S110")
public class ConflictAlertException extends AlertException {

    public ConflictAlertException(String defaultMessage, String entityName, String errorKey) {
        super(Status.CONFLICT, defaultMessage, entityName, errorKey);
    }
}
