package com.example.userservice.util;

import com.example.userservice.constant.SecurityConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public final class SecurityUtil {
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

    private static Optional<String> resolveUserIdFromJwt(Jwt jwt) {
        Object userId = jwt.getClaims().get(SecurityConstant.TOKEN_CLAIM_USER_ID);

        if (userId != null) {
            return Optional.of(userId.toString());
        } else {
            return Optional.empty();
        }
    }
}
