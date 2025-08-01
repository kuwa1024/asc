package com.asc.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.asc.domain.response.FieldError;
import com.asc.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ValidationUtil {
    private static final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validateOrThrow(T request) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            List<FieldError> errors = violations.stream()
                    .map(v -> new FieldError(v.getPropertyPath().toString(), v.getMessage()))
                    .collect(Collectors.toList());
            throw new ValidationException(errors);
        }
    }
}
