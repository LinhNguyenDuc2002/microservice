package com.example.productservice.dto.request;

import com.example.productservice.annotation.product.ValidProductRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ValidProductRequest
public class ProductFormRequest {
    @NotNull(message = "{error.not-null}")
    private ProductRequest product;

    private List<TypeRequest> types;
}
