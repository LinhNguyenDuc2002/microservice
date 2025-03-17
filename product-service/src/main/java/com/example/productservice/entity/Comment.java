package com.example.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Auditor {
    private String id;

    private String message;

    private String imageIds;

    private boolean allowEdit;

    private String customerId;

    private String parentId;

    private String productId;
}
