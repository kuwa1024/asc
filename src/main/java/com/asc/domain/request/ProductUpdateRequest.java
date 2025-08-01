package com.asc.domain.request;

import java.math.BigDecimal;
import org.hibernate.validator.constraints.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class ProductUpdateRequest {

    @NotNull
    @UUID
    private String id;

    @NotBlank
    @Size(max = 10)
    private String name;

    @NotNull
    private BigDecimal price;

    @Size(max = 10)
    private String remarks;

    public String getRemarks() {
        return remarks == null ? "" : remarks;
    }
}
