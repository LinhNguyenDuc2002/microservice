package com.example.userservice.security.oauthserver.token;

import com.example.userservice.constant.OAuthError;
import com.example.userservice.security.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RefreshTokenAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(grantType)) {
            return null;
        }

        MultiValueMap<String, String> parameters = RequestUtils.getParameters(request);

        // refresh_token (REQUIRED)
        String refreshToken = parameters.getFirst(OAuth2ParameterNames.REFRESH_TOKEN);
        if (!StringUtils.hasText(refreshToken)) {
            log.error("Refresh token could not be null/empty");
            throw new OAuth2AuthenticationException(OAuthError.INVALID_REQUEST);
        }

        // Convert to the parameter map
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> additionalParameters.put(key, value.get(0)));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new OAuth2RefreshTokenAuthenticationToken(refreshToken, authentication, Collections.emptySet(), additionalParameters);
    }
}
