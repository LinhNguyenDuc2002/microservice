package com.example.productservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class UserServiceConfiguration {
    public static final String PATH_UUID = "id";

    @Value("${user-service.endpoint}")
    private String baseUrl;

    private String assignRoleUrl;

    private String unassignRoleUrl;

    @PostConstruct
    public void init() {
        assignRoleUrl = String.format("%s/api/role/mapping", baseUrl);
        unassignRoleUrl = String.format("%s/api/role/mapping", baseUrl);
    }
}
