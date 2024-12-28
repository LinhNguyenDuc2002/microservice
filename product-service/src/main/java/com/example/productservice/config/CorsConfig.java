package com.example.productservice.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Cross-Origin Resource Sharing (CORS), allows or rejects requests that come form other source
 */
@Configuration
public class CorsConfig {
    private static final String ALLOWED_ORIGINS = "http://localhost:3000";

    private static final String[] ALLOWED_METHODS = {"OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH", "HEAD"};

    private static final String[] ALLOWED_HEADERS = {
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.AUTHORIZATION
    };

    @Bean
    public FilterRegistrationBean<CorsFilter> customCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Arrays.asList(ALLOWED_METHODS));
        config.setAllowedHeaders(Arrays.asList(ALLOWED_HEADERS));
        config.addAllowedOrigin(ALLOWED_ORIGINS);
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));

        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
