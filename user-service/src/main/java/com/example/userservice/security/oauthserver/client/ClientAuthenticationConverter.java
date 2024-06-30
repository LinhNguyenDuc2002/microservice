package com.example.userservice.security.oauthserver.client;

import com.example.userservice.config.OauthClientConfig;
import com.example.userservice.constant.OAuthError;
import com.example.userservice.constant.SecurityConstant;
import com.example.userservice.security.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * This class converts authentication request from client to Authentication
 */
@Component
@Slf4j
public class ClientAuthenticationConverter implements AuthenticationConverter {
    @Autowired
    private OauthClientConfig oauthClientConfig;
    @Override
    public Authentication convert(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = RequestUtils.getParameters(request);
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

        // check custom grant_type (REQUIRED)
        if ((StringUtils.hasText(oauthClientConfig.getGrantType()) && !oauthClientConfig.getGrantType().equals(grantType))
                && !AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(grantType)) {
            log.error("Invalid grant type: {}", grantType);
            throw new OAuth2AuthenticationException(OAuthError.INVALID_GRANT_TYPE);
        }

        String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID);
        if (!StringUtils.hasText(clientId)) {
            throw new OAuth2AuthenticationException(OAuthError.INVALID_REQUEST);
        }

        String clientSecret = parameters.getFirst(OAuth2ParameterNames.CLIENT_SECRET);
        parameters.remove(OAuth2ParameterNames.CLIENT_ID);
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) ->
                additionalParameters.put(
                        key,
                        value.size() == 1 ? value.get(0) : value.toArray(new String[0])
                ));

        if (StringUtils.hasText(clientSecret)) {
            return new OAuth2ClientAuthenticationToken(clientId, ClientAuthenticationMethod.CLIENT_SECRET_POST, clientSecret, additionalParameters);
        }
        else {
            return new OAuth2ClientAuthenticationToken(clientId, ClientAuthenticationMethod.NONE, null, additionalParameters);
        }
    }
}
