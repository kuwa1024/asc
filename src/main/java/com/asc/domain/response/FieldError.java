package com.asc.domain.response;

import lombok.Value;

@Value
public class FieldError {
    String field;
    String message;
}
