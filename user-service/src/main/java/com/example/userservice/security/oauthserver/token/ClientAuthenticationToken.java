package com.example.userservice.security.oauthserver.token;

import com.example.userservice.security.data.AuthUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ClientAuthenticationToken extends AbstractAuthenticationToken {
    private final RegisteredClient registeredClient;

    private final UserDetails userDetails;

    public ClientAuthenticationToken(RegisteredClient registeredClient, AuthUser userDetails) {
        super(userDetails.getAuthorities());
        this.registeredClient = registeredClient;
        this.userDetails = userDetails;
    }
    @Override
    public Object getCredentials() {
        return registeredClient.getClientSecret();
    }

    @Override
    public Object getPrincipal() {
        return registeredClient.getClientId();
    }
}
