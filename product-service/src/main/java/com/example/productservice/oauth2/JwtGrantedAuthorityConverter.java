package com.example.productservice.oauth2;

import com.example.productservice.constant.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class JwtGrantedAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        // Check Bearer token
        if (!OAuth2AccessToken.TokenType.BEARER.getValue().equals(source.getClaimAsString(SecurityConstant.TOKEN_CLAIM_TYPE))) {
            log.error("Invalid token type");
            throw new OAuth2AuthenticationException("");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (source.hasClaim(SecurityConstant.TOKEN_CLAIM_ROLE)) {
            List<String> roleClaims = source.getClaim(SecurityConstant.TOKEN_CLAIM_ROLE);
            for (String p : roleClaims) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + p));
            }
        }

        return authorities;
    }
}
