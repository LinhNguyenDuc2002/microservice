package com.example.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop extends Auditor {
    private String id;

    private String name;

    private String email;

    private String hotline;

    private String addressId;
}
