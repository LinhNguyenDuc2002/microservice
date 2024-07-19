package com.example.userservice.config;

import com.example.userservice.security.oauthserver.token.AuthTokenCustomizer;
import com.example.userservice.security.token.TokenGenerator;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret-key}")
    public static String secretKey;

    @Value("${jwt.keypair.private}")
    private String privateKey;

    @Value("${jwt.keypair.public}")
    private String publicKey;

    @Value("classpath:keypair/key.pem")
    Resource privateKeyResource;

    @Value("classpath:keypair/cert.pem")
    Resource publicKeyResource;

    @Autowired
    private AuthTokenCustomizer authTokenCustomizer;

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAPublicKey rsaPublicKey;
        RSAPrivateKey rsaPrivateKey;

        try {
            rsaPrivateKey = readPKCS8PrivateKey();
            rsaPublicKey = readX509PublicKey();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .keyID(secretKey)
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    public RSAPrivateKey readPKCS8PrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String key;
        if (StringUtils.hasText(privateKey)) {
            key = privateKey;
        }
        else {
            key = privateKeyResource.getContentAsString(Charset.defaultCharset());
        }

        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public RSAPublicKey readX509PublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String key;
        if (StringUtils.hasText(publicKey)) {
            key = publicKey;
        } else {
            key = publicKeyResource.getContentAsString(Charset.defaultCharset());
        }

        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    @Bean
    public TokenGenerator tokenGenerator() {
        NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
        TokenGenerator tokenGenerator = new TokenGenerator(jwtEncoder);
        tokenGenerator.setJwtCustomizer(authTokenCustomizer);

        return tokenGenerator;
    }
}
