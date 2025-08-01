package com.asc.domain.response;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class ProductGetResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private String remarks;
}
