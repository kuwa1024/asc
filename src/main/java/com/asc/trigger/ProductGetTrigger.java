package com.asc.trigger;

import java.util.Optional;
import org.springframework.stereotype.Component;
import com.asc.domain.request.ProductGetRequest;
import com.asc.domain.response.ProductGetResponse;
import com.asc.function.ProductGet;
import com.asc.util.TriggerResponseHandler;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductGetTrigger {

    private final TriggerResponseHandler responseHandler;
    private final ProductGet productGet;

    @FunctionName("productGet")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "products/{productId}") HttpRequestMessage<Optional<String>> request,
            @BindingName("productId") String productId, ExecutionContext context) {
        try {
            ProductGetRequest param = new ProductGetRequest(productId);
            ProductGetResponse response = productGet.apply(param);
            return responseHandler.buildJsonResponse(request, HttpStatus.OK, response);
        } catch (Exception e) {
            return responseHandler.buildExceptionResponse(e, request, context);
        }
    }
}
