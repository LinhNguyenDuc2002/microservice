package com.example.productservice.mock;

import com.example.productservice.entity.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMock {
    public static Product mockProduct() {
        return Product.builder()
                .name("Test product")
                .quantity(200L)
                .sold(10L)
                .price(100000.0)
                .build();
    }
}
