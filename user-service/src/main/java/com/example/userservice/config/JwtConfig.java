package com.example.userservice.config;

import com.example.userservice.security.oauthserver.token.AuthTokenCustomizer;
import com.example.userservice.security.token.TokenGenerator;
import com.example.userservice.security.util.KeyUtil;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {
    @Value("${jwtToken.secretKey}")
    public static String secretKey;

    @Autowired
    private KeyUtil keyUtil;

    @Autowired
    private AuthTokenCustomizer authTokenCustomizer;

    @Bean
    public JwtEncoder jwtAccessTokenEncoder() {
        JWK jwk = new RSAKey
                .Builder(keyUtil.getAccessTokenPublicKey())
                .privateKey(keyUtil.getAccessTokenPrivateKey())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk)); //Don't allow to change JWKSet after created
        return new NimbusJwtEncoder(jwks); //encode and create JWT token
    }

    @Bean
    public JwtEncoder jwtRefreshTokenEncoder() {
        JWK jwk = new RSAKey
                .Builder(keyUtil.getRefreshTokenPublicKey())
                .privateKey(keyUtil.getRefreshTokenPrivateKey())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtAccessTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtil.getAccessTokenPublicKey()).build();
    }

    @Bean
    public JwtDecoder jwtRefreshTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtil.getRefreshTokenPublicKey()).build();
    }

    @Bean
    public TokenGenerator tokenGenerator() {
        TokenGenerator tokenGenerator = new TokenGenerator(jwtAccessTokenEncoder(), jwtRefreshTokenEncoder());
        tokenGenerator.setJwtCustomizer(authTokenCustomizer);
        return tokenGenerator;
    }

//    /**
//     * Create token from credentials
//     *
//     * @param authentication
//     * @return
//     */
//    public String generateJwtToken(Authentication authentication) {
//        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
//        Date now = new Date();
//
//        String token = Jwts.builder()
//                .setSubject(userPrincipal.getUsername())
//                .claim("id", userPrincipal.getId())
//                .claim("authority", userPrincipal.getAuthorities())
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + expireTime))
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//
//        return token;
//    }
}
