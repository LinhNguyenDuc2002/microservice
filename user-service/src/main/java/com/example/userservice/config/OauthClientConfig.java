package com.example.userservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OauthClientConfig {
    @Value("${oauth.grant-type}")
    private String grantType;

    /**
     * Lifetime of access token
     * In minutes
     */
    @Value("${oauth.client.default.access-token-lifespan:300}")
    private long accessTokenLifeSpanInMin;

    /**
     * Lifetime of refresh token
     * In minutes
     */
    @Value("${oauth.client.default.refresh-token-lifespan:1800}")
    private long refreshTokenLifeSpanInMin;
}
