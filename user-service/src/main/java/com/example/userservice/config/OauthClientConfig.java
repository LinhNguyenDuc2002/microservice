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
     * In minutes
     */
    @Value("${oauth.client.default.access-token-lifespan:1440}")
    private long accessTokenLifeSpanInMin;

    /**
     * In minutes
     */
    @Value("${oauth.client.default.refresh-token-lifespan:1440}")
    private long refreshTokenLifeSpanInMin;
}
