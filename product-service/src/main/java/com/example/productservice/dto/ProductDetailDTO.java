package com.example.productservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class ProductDetailDTO {
    private String id;

    private String name;

    private String description;

    private Integer quantity;

    private Double price;

    private Integer sold;

    private List<String> imageUrls;

    private List<ProductTypeDTO> productTypes;

    private List<FeatureDTO> features;
}
