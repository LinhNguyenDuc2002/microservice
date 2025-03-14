package com.example.orderservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstant {
    public final static String ACCESS_TOKEN_FORMAT = "Bearer %s";

//    --- TOKEN CLAIM ---
    public final static String TOKEN_CLAIM_TYPE = "typ";
    public final static String TOKEN_CLAIM_ROLE = "role";
    public final static String TOKEN_CLAIM_USER_ID = "uid";
    public static final String TOKEN_CLAIM_USERNAME = "username";
}
