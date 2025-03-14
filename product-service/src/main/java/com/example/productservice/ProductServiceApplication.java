package com.example.productservice;

import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties({KeycloakSpringBootProperties.class}) // automatically enable configuration
@ComponentScan(basePackages = {"com.example.productservice", "com.example.servicefoundation"})
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
