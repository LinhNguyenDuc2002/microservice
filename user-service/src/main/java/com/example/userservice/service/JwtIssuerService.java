package com.example.userservice.service;

import com.example.userservice.security.oauthserver.token.ClientAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

public interface JwtIssuerService {
    /**
     * Generate access token
     *
     * @param authentication
     * @return
     */
    OAuth2AccessToken generateAccessToken(ClientAuthenticationToken authentication);

    /**
     * Generate refresh token
     *
     * @param authentication
     * @return
     */
    OAuth2RefreshToken generateRefreshToken(ClientAuthenticationToken authentication);
}
