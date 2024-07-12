package com.example.orderservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ProductConfiguration {
    public static final String PATH_UUID = "id";

    @Value("${product-service.endpoint}")
    private String baseUrl;

    private String productCheckingUrl;

    private String checkWarehouseUrl;

    @PostConstruct
    public void init() {
        productCheckingUrl = String.format("%s/api/product/{%s}/exist", baseUrl, PATH_UUID);
        checkWarehouseUrl = String.format("%s/api/warehouse", baseUrl);
    }
}
