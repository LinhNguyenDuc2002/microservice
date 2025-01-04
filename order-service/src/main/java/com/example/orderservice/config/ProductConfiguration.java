package com.example.orderservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ProductConfiguration {
    public static final String PATH_UUID = "id";
    public static final String QUANTITY = "quantity";

    @Value("${product-service.endpoint}")
    private String baseHost;

    private String baseUrl;

    private String productCheckingUrl;

    private String checkWarehouseUrl;

    private String checkProductUrl;

    private String groupDetailsUrl;

    private String checkShopUrl;

    @PostConstruct
    public void init() {
        baseUrl = String.format("%s/api/product-service", baseHost);
        productCheckingUrl = String.format("%s/product-detail/{%s}/exist", baseUrl, PATH_UUID);
        checkWarehouseUrl = String.format("%s/warehouse", baseUrl);
        groupDetailsUrl = String.format("%s/warehouse/detail-group", baseUrl);
        checkShopUrl = String.format("%s/shop/{%s}/exist", baseUrl, PATH_UUID);
        checkProductUrl = String.format("%s/warehouse/product", baseUrl);
    }
}
