package com.asc.domain.response;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {
    String timestamp;
    int status;
    String error;
    String code;
    String message;
    String path;
    List<FieldError> errors;
}
