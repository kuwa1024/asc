package com.asc.infrastructure.db.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.asc.domain.model.Product;

@Mapper
public interface ProductMapper {

    Product findById(String id);

    void insert(Product product);

    void update(Product product);

    void delete(String id);
}
