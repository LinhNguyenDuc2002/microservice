package com.example.userservice.security.oauthserver.token;

import com.example.userservice.constant.OAuthError;
import com.example.userservice.security.data.AuthUser;
import com.example.userservice.security.data.UserDetailsServiceImpl;
import com.example.userservice.service.JwtIssuerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtIssuerService jwtIssuerService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationRequest clientAuthentication = (CustomAuthenticationRequest) authentication;
        OAuth2ClientAuthenticationToken clientPrincipal = getClientAuthentication(clientAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        if (registeredClient == null) {
            log.error("Unknown oauth client");
            throw new OAuth2AuthenticationException(OAuthError.ACCESS_DENIED);
        }

        // verify user information
        AuthUser user = verifyLoggedInUser(clientAuthentication);

        ClientAuthenticationToken clientAuthenticationToken = new ClientAuthenticationToken(registeredClient, user);
        OAuth2AccessToken accessToken = jwtIssuerService.generateAccessToken(clientAuthenticationToken);
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            refreshToken = jwtIssuerService.generateRefreshToken(clientAuthenticationToken);
        }

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationRequest.class.isAssignableFrom(authentication);
    }

    private OAuth2ClientAuthenticationToken getClientAuthentication(Authentication authentication) {
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

    private AuthUser verifyLoggedInUser(CustomAuthenticationRequest clientAuthentication) {
        String username = clientAuthentication.getAdditionalParameters().get(OAuth2ParameterNames.USERNAME).toString();
        AuthUser user = (AuthUser) userDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new OAuth2AuthenticationException(OAuthError.INVALID_CREDENTIALS);
        }

        // verify the password
        String requestPwd = clientAuthentication.getAdditionalParameters().get(OAuth2ParameterNames.PASSWORD).toString();
        if (!passwordEncoder.matches(requestPwd, user.getPassword())) {
            throw new OAuth2AuthenticationException(OAuthError.INVALID_CREDENTIALS);
        }

        if (!user.isEnabled()) {
            throw new OAuth2AuthenticationException(OAuthError.ACCOUNT_LOCKED);
        }

        return user;
    }
}
