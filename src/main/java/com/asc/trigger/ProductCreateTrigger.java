package com.asc.trigger;

import org.springframework.stereotype.Component;
import com.asc.domain.request.ProductCreateRequest;
import com.asc.domain.response.ProductCreateResponse;
import com.asc.function.ProductCreate;
import com.asc.util.TriggerResponseHandler;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductCreateTrigger {

    private final TriggerResponseHandler responseHandler;
    private final ProductCreate productCreate;

    @FunctionName("productCreate")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "products") HttpRequestMessage<ProductCreateRequest> request,
            ExecutionContext context) {
        try {
            ProductCreateRequest body = request.getBody();
            ProductCreateResponse response = productCreate.apply(body);
            return responseHandler.buildJsonResponse(request, HttpStatus.OK, response);
        } catch (Exception e) {
            return responseHandler.buildExceptionResponse(e, request, context);
        }
    }
}
