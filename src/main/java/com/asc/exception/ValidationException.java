package com.asc.exception;

import java.util.List;
import com.asc.domain.response.FieldError;

public class ValidationException extends RuntimeException {
    private final List<FieldError> fieldErrors;

    public ValidationException(List<FieldError> fieldErrors) {
        super("Validation failed");
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
