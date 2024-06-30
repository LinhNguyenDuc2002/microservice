package com.example.userservice.security.oauthserver.token;

import com.example.userservice.constant.OAuthError;
import com.example.userservice.constant.SecurityConstant;
import com.example.userservice.security.data.AuthUser;
import com.example.userservice.service.JwtIssuerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private JwtDecoder jwtRefreshTokenDecoder;

    @Autowired
    private JwtIssuerService jwtIssuerService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2RefreshTokenAuthenticationToken clientAuthentication = (OAuth2RefreshTokenAuthenticationToken) authentication;
        OAuth2ClientAuthenticationToken clientPrincipal = getClientAuthentication(clientAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        if (registeredClient == null) {
            log.error("Unknown oauth client");
            throw new OAuth2AuthenticationException(OAuthError.ACCESS_DENIED);
        }

        // Validate refresh token
        String refreshToken = clientAuthentication.getRefreshToken();
        Jwt jwt;
        try {
            jwt = this.jwtRefreshTokenDecoder.decode(refreshToken);
        } catch (Exception ex) {
            log.error("Invalid refresh token: {}", ex.getMessage());
            throw new OAuth2AuthenticationException(OAuthError.INVALID_REQUEST);
        }

        // Validate token type
        if (!SecurityConstant.TOKEN_CLAIM_REFRESH_VALUE.equals(jwt.getClaimAsString(SecurityConstant.TOKEN_CLAIM_TYPE))) {
            log.error("Invalid token type");
            throw new OAuth2AuthenticationException(OAuthError.INVALID_REQUEST);
        }

        // Validate token expiration
        Instant expireTime = jwt.getExpiresAt();
        if (expireTime == null || expireTime.isBefore(Instant.now())) {
            log.error("Refresh token has expired");
            throw new OAuth2AuthenticationException(OAuthError.INVALID_REQUEST);
        }

        // Verify user information
        String username = jwt.getClaimAsString(SecurityConstant.TOKEN_CLAIM_USERNAME);
        AuthUser user = (AuthUser) userDetailsService.loadUserByUsername(username);

        // Generate new token
        ClientAuthenticationToken clientAuthenticationToken = new ClientAuthenticationToken(registeredClient, user);
        OAuth2AccessToken accessToken = jwtIssuerService.generateAccessToken(clientAuthenticationToken);
        OAuth2RefreshToken newRefreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            newRefreshToken = jwtIssuerService.generateRefreshToken(clientAuthenticationToken);
        }

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, newRefreshToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static OAuth2ClientAuthenticationToken getClientAuthentication(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        log.error("Invalid oauth2 authentication type: {}", authentication.getPrincipal().getClass().getName());
        throw new OAuth2AuthenticationException(OAuthError.INVALID_CLIENT);
    }
}
