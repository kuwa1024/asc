package com.asc.function;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.asc.application.ProductService;
import com.asc.domain.model.Product;
import com.asc.domain.request.ProductCreateRequest;
import com.asc.domain.response.ProductCreateResponse;
import com.asc.util.ValidationUtil;

@Component
public class ProductCreate implements Function<ProductCreateRequest, ProductCreateResponse> {

    @Autowired
    private ProductService productService;

    @Override
    public ProductCreateResponse apply(ProductCreateRequest request) {
        ValidationUtil.validateOrThrow(request);
        Product newProduct = productService.createProduct(request.getName(), request.getPrice(),
                request.getRemarks());
        return new ProductCreateResponse(newProduct.getId());
    }
}
