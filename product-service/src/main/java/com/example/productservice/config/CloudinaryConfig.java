package com.example.productservice.config;

import com.cloudinary.Cloudinary;
import com.example.productservice.constant.CloudinaryConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloudName}")
    private String cloudName;

    @Value("${cloudinary.apiKey}")
    private String apiKey;

    @Value("${cloudinary.apiSecret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map config = new HashMap();
        config.put(CloudinaryConstant.CLOUD_NAME, cloudName);
        config.put(CloudinaryConstant.API_KEY, apiKey);
        config.put(CloudinaryConstant.API_SECRET, apiSecret);
        config.put(CloudinaryConstant.SECURE, true);

        return new Cloudinary(config);
    }
}
