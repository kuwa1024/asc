package com.asc.domain.repository;

import java.util.Optional;
import com.asc.domain.model.Product;

public interface ProductRepository {

    void save(Product product);

    Optional<Product> findById(String id);

    void update(Product product);

    void delete(String id);
}
