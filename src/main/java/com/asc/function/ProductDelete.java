package com.asc.function;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.asc.application.ProductService;
import com.asc.domain.request.ProductDeleteRequest;
import com.asc.util.ValidationUtil;

@Component
public class ProductDelete implements Function<ProductDeleteRequest, Void> {

    @Autowired
    private ProductService productService;

    @Override
    public Void apply(ProductDeleteRequest request) {
        ValidationUtil.validateOrThrow(request);
        productService.deleteProduct(request.getId());
        return null;
    }
}
