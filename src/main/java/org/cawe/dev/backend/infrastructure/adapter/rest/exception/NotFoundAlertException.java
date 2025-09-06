package org.cawe.dev.backend.infrastructure.adapter.rest.exception;

import org.zalando.problem.Status;

@SuppressWarnings("java:S110")
public class NotFoundAlertException extends AlertException {

    public NotFoundAlertException(String defaultMessage, String entityName, String errorKey) {
        super(Status.NOT_FOUND, defaultMessage, entityName, errorKey);
    }
}
