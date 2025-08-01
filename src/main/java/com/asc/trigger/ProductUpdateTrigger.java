package com.asc.trigger;

import org.springframework.stereotype.Component;
import com.asc.domain.request.ProductUpdateRequest;
import com.asc.domain.response.ProductUpdateResponse;
import com.asc.function.ProductUpdate;
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
public class ProductUpdateTrigger {

    private final TriggerResponseHandler responseHandler;
    private final ProductUpdate productUpdate;

    @FunctionName("productUpdate")
    public HttpResponseMessage run(@HttpTrigger(name = "req", methods = {HttpMethod.PUT},
            authLevel = AuthorizationLevel.ANONYMOUS,
            route = "products/{productId}") HttpRequestMessage<ProductUpdateRequest> request,
            @BindingName("productId") String productId, ExecutionContext context) {
        try {
            ProductUpdateRequest body =
                    new ProductUpdateRequest(productId, request.getBody().getName(),
                            request.getBody().getPrice(), request.getBody().getRemarks());
            ProductUpdateResponse response = productUpdate.apply(body);
            return responseHandler.buildJsonResponse(request, HttpStatus.OK, response);
        } catch (Exception e) {
            return responseHandler.buildExceptionResponse(e, request, context);
        }
    }
}
