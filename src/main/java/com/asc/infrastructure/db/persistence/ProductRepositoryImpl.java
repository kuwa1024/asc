package com.asc.infrastructure.db.persistence;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.asc.domain.model.Product;
import com.asc.domain.repository.ProductRepository;
import com.asc.infrastructure.db.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductMapper productMapper;

    @Override
    public void save(Product product) {
        productMapper.insert(product);
    }

    @Override
    public Optional<Product> findById(String id) {
        Product product = productMapper.findById(id);
        return Optional.ofNullable(product);
    }

    @Override
    public void update(Product product) {
        productMapper.update(product);
    }

    @Override
    public void delete(String id) {
        productMapper.delete(id);
    }
}
