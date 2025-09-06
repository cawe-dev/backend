package org.cawe.dev.backend.domain.exception;

import org.cawe.dev.backend.infrastructure.adapter.rest.exception.UnauthorizedAlertException;

@SuppressWarnings("java:S110")
public class UserIdentityNotFoundException extends UnauthorizedAlertException {

    private static final String ERROR_MESSAGE = "Could not retrieve user info from external identity provider.";
    private static final String ENTITY_NAME = "user";
    private static final String ERROR_KEY = "identity-not-found";

    public UserIdentityNotFoundException() {
        super(ERROR_MESSAGE, ENTITY_NAME, ERROR_KEY);
    }
}
