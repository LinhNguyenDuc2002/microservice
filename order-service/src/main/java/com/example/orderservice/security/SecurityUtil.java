package com.example.orderservice.security;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.constant.SecurityConstant;
import com.example.orderservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

@Slf4j
public class SecurityUtil {
    /**
     * Get current user id
     *
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

    /**
     * Get the current logged-in  user
     *
     * @return current username if user has logged. Otherwise return {@link Optional#empty()}
     */
    public static Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return Optional.empty();
        }

        // Resolve username depend on type of principal.
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return Optional.of(principal.toString());
        } else if (principal instanceof Jwt jwt) {
            return resolveUsernameFromJwt(jwt);
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
        throw new NotFoundException(I18nMessage.ERROR_TOKEN_NOT_FOUND);
    }

    private static Optional<String> resolveUserIdFromJwt(Jwt jwt) {
        Object userId = jwt.getClaims().get(SecurityConstant.TOKEN_CLAIM_USER_ID);

        if (userId != null) {
            return Optional.of(userId.toString());
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> resolveUsernameFromJwt(Jwt jwt) {
        Object username = jwt.getClaims().get(SecurityConstant.TOKEN_CLAIM_USERNAME);

        if (username != null) {
            return Optional.of(username.toString());
        } else {
            return Optional.empty();
        }
    }
}
