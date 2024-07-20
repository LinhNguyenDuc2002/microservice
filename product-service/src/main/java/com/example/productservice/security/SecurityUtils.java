package com.example.productservice.security;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.constant.SecurityConstant;
import com.example.productservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

@Slf4j
public class SecurityUtils {
    /**
     * Get UserId current
     * @return
     */
    public static Optional<String> getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return Optional.empty();
        }

        // Resolve username depend on type of principal.
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return resolveUserIdFromJwt(jwt);
        } else {
            return Optional.empty();
        }
    }

    public static String getCurrentJWT() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getToken().getTokenValue();
        }

        log.error("error at getCurrentJWT: cannot get current JWT");
        throw new NotFoundException(ExceptionMessage.ERROR_TOKEN_NOT_FOUND);
    }

    private static Optional<String> resolveUserIdFromJwt(Jwt jwt) {
        Object userId = jwt.getClaims().get(SecurityConstant.TOKEN_CLAIM_USER_ID);

        if (userId != null) {
            return Optional.of(userId.toString());
        } else {
            return Optional.empty();
        }
    }
}
