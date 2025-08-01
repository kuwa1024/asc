package com.asc.trigger;

import java.util.Optional;
import org.springframework.stereotype.Component;
import com.asc.domain.request.ProductDeleteRequest;
import com.asc.function.ProductDelete;
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
public class ProductDeleteTrigger {

    private final TriggerResponseHandler responseHandler;
    private final ProductDelete productDelete;

    @FunctionName("productDelete")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.DELETE},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "products/{productId}") HttpRequestMessage<Optional<String>> request,
            @BindingName("productId") String productId, ExecutionContext context) {
        try {
            ProductDeleteRequest param = new ProductDeleteRequest(productId);
            productDelete.apply(param);
            return responseHandler.buildJsonResponse(request, HttpStatus.NO_CONTENT, null);
        } catch (Exception e) {
            return responseHandler.buildExceptionResponse(e, request, context);
        }
    }
}
