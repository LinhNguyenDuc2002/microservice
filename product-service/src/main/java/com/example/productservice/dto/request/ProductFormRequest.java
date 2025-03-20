package com.example.productservice.dto.request;

import com.example.productservice.annotation.product.ValidProductRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//@ValidProductRequest
public class ProductFormRequest {
    @NotNull(message = "{error.not-null}")
    private ProductRequest product;

    private List<TypeRequest> types;
}