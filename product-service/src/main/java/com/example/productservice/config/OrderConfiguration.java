package com.example.productservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OrderConfiguration {
    public static final String PATH_UUID = "id";

    @Value("${order-service.endpoint}")
    private String baseUrl;

    private String checkDetailUrl;

    @PostConstruct
    public void init() {
        checkDetailUrl = String.format("%s/api/detail/{%s}/exist", baseUrl, PATH_UUID);
    }
}
