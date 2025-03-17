package com.example.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductType extends Auditor {
    private String id;

    private String name;

    private String imageId;

    private String description;

    private Integer quantity;

    private Double price;

    private Boolean status;

    private String productId;
}
