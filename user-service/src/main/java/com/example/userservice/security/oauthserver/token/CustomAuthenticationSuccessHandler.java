package com.example.userservice.security.oauthserver.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AccessTokenAuthenticationToken authenticationToken = (OAuth2AccessTokenAuthenticationToken) authentication;
        setTokenResponse(response, authenticationToken);
    }

    private void setTokenResponse(HttpServletResponse response, OAuth2AccessTokenAuthenticationToken authentication) throws IOException {
        OAuth2AccessToken accessToken = authentication.getAccessToken();
        OAuth2RefreshToken refreshToken = authentication.getRefreshToken();
        Map<String, Object> additionalParameters = authentication.getAdditionalParameters();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", accessToken.getTokenValue());
        parameters.put("token_type", accessToken.getTokenType().getValue());
        parameters.put("expires_in", getExpiresIn(accessToken));
        if (!CollectionUtils.isEmpty(accessToken.getScopes())) {
            parameters.put("scope", StringUtils.collectionToDelimitedString(accessToken.getScopes(), " "));
        }

        if (refreshToken != null) {
            parameters.put("refresh_token", refreshToken.getTokenValue());
        }

        if (!CollectionUtils.isEmpty(additionalParameters)) {
            parameters.putAll(additionalParameters);
        }

        byte[] bytes = objectMapper.writeValueAsString(parameters).getBytes(StandardCharsets.UTF_8);
        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(new String(bytes, StandardCharsets.ISO_8859_1));
        response.flushBuffer();
    }

    /**
     * Calculate the remaining time
     * @param token
     * @return
     */
    private static long getExpiresIn(AbstractOAuth2Token token) {
        return token.getExpiresAt() != null ? ChronoUnit.MILLIS.between(Instant.now(), token.getExpiresAt()) : -1L;
    }
}
