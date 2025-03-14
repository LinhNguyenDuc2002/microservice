package com.example.userservice.security.oauthserver.token;

import com.example.userservice.constant.SecurityConstant;
import com.example.userservice.security.data.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to customize the token claims (a mapper can be defined to map the user attributes to token claims)
 */
@Component
@Slf4j
public class AuthTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    @Override
    public void customize(JwtEncodingContext context) {
        ClientAuthenticationToken clientAuthenticationToken = context.getPrincipal();

        // Add user info
        UserDetails userDetails = clientAuthenticationToken.getUserDetails();
        if (userDetails instanceof AuthUser authUser) {
            context.getClaims().claims(claims -> {
                claims.put(SecurityConstant.TOKEN_CLAIM_USER_ID, authUser.getId());
                claims.put(SecurityConstant.TOKEN_CLAIM_USERNAME, authUser.getUsername());
                claims.put(SecurityConstant.TOKEN_CLAIM_EMAIL, authUser.getEmail());
            });

            // Customize access token
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> {
                    Set<String> roles = new HashSet<>();
                    for (GrantedAuthority authority : authUser.getAuthorities()) {
                        roles.add(authority.getAuthority());
                    }

                    claims.put(SecurityConstant.TOKEN_CLAIM_ROLE, roles);
                });
            }
        }
    }
}
