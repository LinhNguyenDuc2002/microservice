package com.example.productservice.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRequest {
    @NotNull
    private String name;

    private Double price;

    private Long quantity;

    private String note;

    @NotNull
    private String category;

    // List interface don't have a specific constructor, so should user Collection instead of
    private Collection<ProductTypeRequest> productTypes;
}
