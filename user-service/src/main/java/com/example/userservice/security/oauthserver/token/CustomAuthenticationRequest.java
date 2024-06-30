package com.example.userservice.security.oauthserver.token;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * Custom the authentication token
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class CustomAuthenticationRequest extends OAuth2AuthorizationGrantAuthenticationToken {
    public CustomAuthenticationRequest(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(additionalParameters.get(OAuth2ParameterNames.GRANT_TYPE).toString()),
                clientPrincipal,
                additionalParameters);
    }
}
