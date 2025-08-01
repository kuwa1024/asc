package com.asc.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.asc.domain.response.ErrorResponse;
import com.asc.domain.response.FieldError;
import com.microsoft.azure.functions.HttpStatus;

public class ErrorResponseFactory {

    public static ErrorResponse buildValidationErrorResponse(List<FieldError> fieldErrors,
            String path) {
        return ErrorResponse.builder()
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .status(HttpStatus.BAD_REQUEST.value()).error(HttpStatus.BAD_REQUEST.toString())
                .code("VALIDATION_ERROR").message("入力内容に誤りがあります。").path(path).errors(fieldErrors)
                .build();
    }

    public static ErrorResponse buildNotFoundErrorResponse(String message, String path) {
        return ErrorResponse.builder()
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .status(HttpStatus.NOT_FOUND.value()).error(HttpStatus.NOT_FOUND.toString())
                .code("NOT_FOUND").message(message).path(path).build();
    }

    public static ErrorResponse buildServerErrorResponse(String path) {
        return ErrorResponse.builder()
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.toString()).code("INTERNAL_ERROR")
                .message("予期しないエラーが発生しました。").path(path).build();
    }
}
