package org.cawe.dev.backend.infrastructure.adapter.rest.exception;

import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

import java.util.HashMap;
import java.util.Map;

@Getter
@SuppressWarnings("java:S110")
public abstract class AlertException extends AbstractThrowableProblem {

    private final String entityName;
    private final String errorKey;

    protected AlertException(StatusType status, String defaultMessage, String entityName, String errorKey) {
        super(
                ErrorConstants.DEFAULT_TYPE,
                defaultMessage,
                status,
                null,
                null,
                null,
                getAlertParameters(entityName, errorKey)
        );
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
