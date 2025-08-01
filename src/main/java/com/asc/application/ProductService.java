package com.asc.application;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.asc.domain.model.Product;
import com.asc.domain.repository.ProductRepository;
import com.asc.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(String name, BigDecimal price, String remarks) {
        String newId = UUID.randomUUID().toString();

        Product product = new Product(newId, name, price, remarks);

        productRepository.save(product);

        return product;
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("商品が見つかりません。"));
    }

    public Product updateProduct(String id, String name, BigDecimal price, String remarks) {
        Product product = getProductById(id);

        Product newProduct = new Product(product.getId(), name, price, remarks);

        productRepository.update(newProduct);

        return newProduct;
    }

    public void deleteProduct(String id) {
        Product product = getProductById(id);
        productRepository.delete(product.getId());
    }
}
