package com.example.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EntityListeners(ProductListener.class)
public class Product extends Auditor {
    private String id;

    private String name;

    private Double price;

    private Integer quantity;

    private Integer sold;

    private String description;

    private Boolean status;

    private String imageIds;

    private String categoryId;
}
