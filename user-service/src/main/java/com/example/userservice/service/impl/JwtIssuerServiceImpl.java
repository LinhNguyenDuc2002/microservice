package com.example.userservice.service.impl;

import com.example.userservice.config.OauthClientConfig;
import com.example.userservice.constant.OAuthError;
import com.example.userservice.security.oauthserver.token.ClientAuthenticationToken;
import com.example.userservice.security.token.TokenGenerator;
import com.example.userservice.service.JwtIssuerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtIssuerServiceImpl implements JwtIssuerService {
    @Autowired
    private OauthClientConfig oauthClientConfig;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Override
    public OAuth2AccessToken generateAccessToken(ClientAuthenticationToken authentication) {
        OAuth2Token generatedAccessToken = generateToken(authentication, OAuth2TokenType.ACCESS_TOKEN);
        return new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(),
                generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(),
                authentication.getRegisteredClient().getScopes()
        );
    }

    @Override
    public OAuth2RefreshToken generateRefreshToken(ClientAuthenticationToken authentication) {
        OAuth2Token generatedRefreshToken = generateToken(authentication, OAuth2TokenType.REFRESH_TOKEN);
        return new OAuth2RefreshToken(
                generatedRefreshToken.getTokenValue(),
                generatedRefreshToken.getIssuedAt(),
                generatedRefreshToken.getExpiresAt()
        );
    }

    private OAuth2Token generateToken(ClientAuthenticationToken authentication, OAuth2TokenType type) {
        final RegisteredClient registeredClient = authentication.getRegisteredClient();
        if (registeredClient == null) {
            throw new OAuth2AuthenticationException(OAuthError.ACCESS_DENIED);
        }

        // Token builder
        OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(registeredClient.getScopes())
                .authorizationGrantType(new AuthorizationGrantType(oauthClientConfig.getGrantType()))
                .tokenType(type)
                .build();

        // Generate token
        OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            log.error("Failed to generate access token");
            throw new OAuth2AuthenticationException(OAuthError.SERVER_ERROR);
        }

        return generatedAccessToken;
    }
}
