package com.asc.util;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import com.asc.domain.response.ErrorResponse;
import com.asc.domain.response.FieldError;
import com.asc.exception.NotFoundException;
import com.asc.exception.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TriggerResponseHandler {

    private final ObjectMapper objectMapper;

    public HttpResponseMessage buildExceptionResponse(Exception e, HttpRequestMessage<?> request,
            ExecutionContext context) {
        Logger logger = context.getLogger();
        String path = request.getUri().getPath();

        try {
            ErrorResponse errorResponse;

            if (e instanceof ValidationException) {
                List<FieldError> errors = ((ValidationException) e).getFieldErrors();
                errorResponse = ErrorResponseFactory.buildValidationErrorResponse(errors, path);
                return buildJsonResponse(request, HttpStatus.BAD_REQUEST, errorResponse);
            } else if (e instanceof NotFoundException) {
                errorResponse =
                        ErrorResponseFactory.buildNotFoundErrorResponse(e.getMessage(), path);
                return buildJsonResponse(request, HttpStatus.NOT_FOUND, errorResponse);
            }

            logger.log(Level.SEVERE, "Unhandled exception occurred", e);
            errorResponse = ErrorResponseFactory.buildServerErrorResponse(path);
            return buildJsonResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, errorResponse);

        } catch (Exception serializationEx) {
            logger.log(Level.SEVERE, "Error serializing error response", serializationEx);
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"Error serialization failed\", \"detail\":\""
                            + serializationEx.getMessage() + "\"}")
                    .build();
        }
    }

    public <T> HttpResponseMessage buildJsonResponse(HttpRequestMessage<?> request,
            HttpStatus status, T body) throws JsonProcessingException {
        HttpResponseMessage.Builder builder =
                request.createResponseBuilder(status).header("Content-Type", "application/json");

        if (body != null) {
            builder.body(objectMapper.writeValueAsString(body));
        }

        return builder.build();
    }
}
