package com.example.productservice.mock;

import com.example.productservice.entity.Category;
import com.example.productservice.dto.request.CategoryRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMock {
    public static CategoryRequest mockCategoryRequest() {
        return CategoryRequest.builder()
                .name("Clothes")
                .note("Test case")
                .build();
    }

    public static Category mockCategory() {
        return Category.builder()
                .name("Clothes")
                .note("Test case")
                .build();
    }
}
