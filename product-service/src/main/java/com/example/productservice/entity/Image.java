package com.example.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image extends Auditor {
    private String id;

    private String format;

    private String resourceType;

    private String secureUrl;

    private String productId;

    private String commentId;

    private String productTypeId;
}
