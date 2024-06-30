package com.example.userservice.security.resourceserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This class is used to verify and create client authentication
 */
@Component
@Slf4j
public class JwtResourceServerCustomize implements Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer> {
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * Customize JwtConfigurer
     * @param jwtConfigurer
     */
    @Override
    public void customize(OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwtConfigurer) {
        String key = UUID.randomUUID().toString();
        AnonymousAuthenticationProvider anonymousAuthenticationProvider = new AnonymousAuthenticationProvider(key);
        ProviderManager providerManager = new ProviderManager(this.jwtAuthenticationProvider, anonymousAuthenticationProvider);
        jwtConfigurer.authenticationManager(providerManager);
    }
}
