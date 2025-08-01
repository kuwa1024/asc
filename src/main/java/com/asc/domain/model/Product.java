package com.asc.domain.model;

import java.math.BigDecimal;
import lombok.NonNull;
import lombok.Value;

@Value
public class Product {

    @NonNull
    String id;

    @NonNull
    String name;

    @NonNull
    BigDecimal price;

    String remarks;
}
