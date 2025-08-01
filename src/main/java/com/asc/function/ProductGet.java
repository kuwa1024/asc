package com.asc.function;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.asc.application.ProductService;
import com.asc.domain.model.Product;
import com.asc.domain.request.ProductGetRequest;
import com.asc.domain.response.ProductGetResponse;
import com.asc.util.ValidationUtil;

@Component
public class ProductGet implements Function<ProductGetRequest, ProductGetResponse> {

    @Autowired
    private ProductService productService;

    @Override
    public ProductGetResponse apply(ProductGetRequest request) {
        ValidationUtil.validateOrThrow(request);
        Product product = productService.getProductById(request.getId());
        return new ProductGetResponse(product.getId(), product.getName(), product.getPrice(),
                product.getRemarks());
    }
}
