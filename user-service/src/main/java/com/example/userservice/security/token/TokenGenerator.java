package com.example.userservice.security.token;

import com.example.userservice.constant.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

/**
 * Generate token
 * --- HEADER ---
 * typ (type)
 * alg (algorithm)
 * --- PAYLOAD ---
 * jti (jwt id)
 * sub (subject)
 * aud(audience)
 * iss (issuer)
 * exp (expiration time)
 * iat (issued at)
 * nbf (not before)
 * claims:
 *      cli (client id)
 *      typ (type, eg: Bearer)
 *      scope
 */
@Slf4j
public class TokenGenerator implements OAuth2TokenGenerator<Jwt> {
    private final JwtEncoder jwtEncoder;

    private OAuth2TokenCustomizer<JwtEncodingContext> customizer;

    public TokenGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public Jwt generate(OAuth2TokenContext context) {
        // Validate token type
        final OAuth2TokenType tokenType = context.getTokenType();
        if (!OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)
                && !OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            log.error("Invalid token type");
            return null;
        }

        // Get registered client in context
        final RegisteredClient registeredClient = context.getRegisteredClient();

        // Initialize token builder
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString()) // jti
                .subject(registeredClient.getId()) // owner object
                .audience(Collections.singletonList(registeredClient.getClientId()));
        claimsBuilder.claim(SecurityConstant.TOKEN_CLAIM_CLIENT_ID, registeredClient.getClientId());

        // Get issuer and add claim iss (issuer)
        String issuer = null;
        if (context.getAuthorizationServerContext() != null) {
            issuer = context.getAuthorizationServerContext().getIssuer();
        }
        if (StringUtils.hasText(issuer)) {
            claimsBuilder.issuer(issuer); // release source
        }

        // Jwt algorithm in header: typ (type), alg (algorithm)
        JwsHeader.Builder jwsHeaderBuilder = JwsHeader.with(SignatureAlgorithm.RS256)
                .type("JWT");

        // Adding customizer
        if (this.customizer != null) {
            JwtEncodingContext.Builder jwtContextBuilder = JwtEncodingContext.with(jwsHeaderBuilder, claimsBuilder)
                    .registeredClient(context.getRegisteredClient())
                    .principal(context.getPrincipal())
                    .authorizationServerContext(context.getAuthorizationServerContext())
                    .authorizedScopes(context.getAuthorizedScopes())
                    .tokenType(context.getTokenType())
                    .authorizationGrantType(context.getAuthorizationGrantType());

            if (context.getAuthorization() != null) {
                jwtContextBuilder.authorization(context.getAuthorization());
            }

            if (context.getAuthorizationGrant() != null) {
                jwtContextBuilder.authorizationGrant(context.getAuthorizationGrant());
            }

            JwtEncodingContext jwtContext = jwtContextBuilder.build();
            this.customizer.customize(jwtContext);
        }

        // Define the token claims based on token type
        Instant issuedAt = Instant.now();
        if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            Duration lifespanInMinute = registeredClient.getTokenSettings().getAccessTokenTimeToLive(); // represents a period of time
            claimsBuilder.claim(SecurityConstant.TOKEN_CLAIM_TYPE, OAuth2AccessToken.TokenType.BEARER.getValue());

            // token scope
            if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
                claimsBuilder.claim(SecurityConstant.TOKEN_CLAIM_SCOPE, StringUtils.arrayToDelimitedString(context.getAuthorizedScopes().toArray(), " "));
            }

            Instant expiresAt = issuedAt.plus(lifespanInMinute);
            claimsBuilder.issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .notBefore(issuedAt); // the moment when JWT can be used

            JwsHeader jwsHeader = jwsHeaderBuilder.build();
            JwtClaimsSet claims = claimsBuilder.build();
            return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
        }
        else { // REFRESH TOKEN
            Duration lifespanInMinute = registeredClient.getTokenSettings().getRefreshTokenTimeToLive();
            claimsBuilder.claim(SecurityConstant.TOKEN_CLAIM_TYPE, SecurityConstant.TOKEN_CLAIM_REFRESH_VALUE);

            Instant expiresAt = issuedAt.plus(lifespanInMinute);
            claimsBuilder.issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .notBefore(issuedAt); // The moment when JWT can be used

            JwsHeader jwsHeader = jwsHeaderBuilder.build();
            JwtClaimsSet claims = claimsBuilder.build();
            return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
        }
    }

    /**
     * Set customizer
     * @param customizer customizer to update the token claims
     */
    public void setJwtCustomizer(OAuth2TokenCustomizer<JwtEncodingContext> customizer) {
        this.customizer = customizer;
    }
}
