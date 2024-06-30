package com.example.userservice.security.data;

import com.example.userservice.config.OauthClientConfig;
import com.example.userservice.entity.OauthClient;
import com.example.userservice.entity.OauthClientDetail;
import com.example.userservice.repository.OauthClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@Slf4j
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {
    @Autowired
    private OauthClientConfig oauthClientConfig;

    @Autowired
    private OauthClientRepository oauthClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        OauthClient oauthClient = oauthClientRepository.findByClientId(clientId);
        if (oauthClient == null) {
            log.error("No client was found by client id: {}", clientId);
            return null;
        }

        return fromOauthClient(oauthClient);
    }

    private RegisteredClient fromOauthClient(OauthClient client) {
        OauthClientDetail clientDetail = client.getDetail();
//        String clientSecret = null;
//        ClientAuthenticationMethod authMethod = ClientAuthenticationMethod.NONE;
//
//        if (OauthClientType.CONFIDENTIAL.name().equals(client.getClientType())) {
//            clientSecret = passwordEncoder.encode(client.getClientSecret());
//            authMethod = ClientAuthenticationMethod.CLIENT_SECRET_POST;
//        }

        // get access token lifespan
        Long clientTokenLifespan = clientDetail.getTokenLifespanInMinute();
        long accessTokenLifespan = (clientTokenLifespan == null || clientTokenLifespan <= 0) ? oauthClientConfig.getAccessTokenLifeSpanInMin() : clientTokenLifespan;

        // get refresh token lifespan
        Long clientRefreshTokenLifespan = clientDetail.getRefreshTokenLifespanInMinute();
        long refreshTokenLifespan = (clientRefreshTokenLifespan == null || clientRefreshTokenLifespan <= 0) ? oauthClientConfig.getRefreshTokenLifeSpanInMin() : clientRefreshTokenLifespan;

        return RegisteredClient.withId(client.getUuid())
                .clientId(client.getClientId())
                .clientSecret(passwordEncoder.encode(client.getClientSecret()))
                .clientName(client.getClientId())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(new AuthorizationGrantType(oauthClientConfig.getGrantType()))
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUris(s -> s.addAll(clientDetail.getRedirectUris()))
                .scope(client.getClientId())
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .requireProofKey(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofMinutes(accessTokenLifespan))
                        .refreshTokenTimeToLive(Duration.ofMinutes(refreshTokenLifespan))
                        .reuseRefreshTokens(true)
                        .build())
                .build();
    }
}
