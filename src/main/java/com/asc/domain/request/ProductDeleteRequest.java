package com.asc.domain.request;

import org.hibernate.validator.constraints.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ProductDeleteRequest {

    @NotNull
    @UUID
    private String id;
}
