package com.asc.function;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.asc.application.ProductService;
import com.asc.domain.model.Product;
import com.asc.domain.request.ProductUpdateRequest;
import com.asc.domain.response.ProductUpdateResponse;
import com.asc.util.ValidationUtil;

@Component
public class ProductUpdate implements Function<ProductUpdateRequest, ProductUpdateResponse> {

    @Autowired
    private ProductService productService;

    @Override
    public ProductUpdateResponse apply(ProductUpdateRequest request) {
        ValidationUtil.validateOrThrow(request);
        Product newProduct = productService.updateProduct(request.getId(), request.getName(),
                request.getPrice(), request.getRemarks());
        return new ProductUpdateResponse(newProduct.getId(), newProduct.getName(),
                newProduct.getPrice(), newProduct.getRemarks());
    }
}
