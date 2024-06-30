package com.example.userservice.security.oauthserver.token;

import com.example.userservice.config.OauthClientConfig;
import com.example.userservice.constant.OAuthError;
import com.example.userservice.constant.SecurityConstant;
import com.example.userservice.security.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class is used to pre-process the oauth2/token request
 * It will customize the authentication based on our grant_type
 */
@Component
@Slf4j
public class TokenAuthenticationConverter implements AuthenticationConverter {
    @Autowired
    private OauthClientConfig oauthClientConfig;

    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (StringUtils.hasText(oauthClientConfig.getGrantType()) && !oauthClientConfig.getGrantType().equals(grantType)) {
            return null;
        }

        MultiValueMap<String, String> parameters = RequestUtils.getParameters(request);

        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            log.error("Username could not be null/empty");
            throw new OAuth2AuthenticationException(OAuthError.INVALID_REQUEST);
        }

        // password (REQUIRED)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            log.error("Password could not be null/empty");
            throw new OAuth2AuthenticationException(OAuthError.INVALID_REQUEST);
        }

        // Get client ip address
        String ipAddress;
        String xForwardedForHeader = request.getHeader(SecurityConstant.HEADER_FORWARDED_ADDRESS);
        if (xForwardedForHeader == null) {
            ipAddress = request.getRemoteAddr();
        }
        else {
            ipAddress = new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }

        // Get the origin
        String origin = request.getHeader(SecurityConstant.HEADER_ORIGIN);
        if (!StringUtils.hasText(origin)) {
            log.error("Header origin is missing");
            throw new OAuth2AuthenticationException(OAuthError.HEADER_MISSING);
        }

        // Convert to the parameter map
        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put(SecurityConstant.AUTH_PARAM_CLIENT_IP, ipAddress);
        additionalParameters.put(SecurityConstant.AUTH_PARAM_ORIGIN, origin);
        parameters.forEach((key, value) -> additionalParameters.put(key, value.get(0)));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new CustomAuthenticationRequest(authentication, additionalParameters);
    }
}
