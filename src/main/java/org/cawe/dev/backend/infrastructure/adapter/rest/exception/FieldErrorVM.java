package org.cawe.dev.backend.infrastructure.adapter.rest.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class FieldErrorVM implements Serializable {

    private final String objectName;

    private final String field;

    private final String message;

    public FieldErrorVM(String dto, String field, String message) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
    }

}
