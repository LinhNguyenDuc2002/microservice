package com.example.productservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OrderServiceConfiguration {
    public static final String PATH_UUID = "id";

    @Value("${order-service.endpoint}")
    private String baseUrl;

    private String checkDetailUrl;

    private String calculateSalesUrl;

    @PostConstruct
    public void init() {
        checkDetailUrl = String.format("%s/api/detail/{%s}/exist", baseUrl, PATH_UUID);
        calculateSalesUrl = String.format("%s/api/dashboard/sales", baseUrl);
    }
}
