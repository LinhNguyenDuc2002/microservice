package com.example.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address extends Auditor {
    private String id;

    private String detail;

    private String ward;

    private String district;

    private String city;

    private String country;
}
