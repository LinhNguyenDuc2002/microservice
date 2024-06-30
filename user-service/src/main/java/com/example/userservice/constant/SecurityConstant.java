package com.example.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstant {
    public static final String ADMIN = "ADMIN";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String CUSTOMER = "CUSTOMER";

//    --- HEADER PARAM ---
    public static final String HEADER_FORWARDED_ADDRESS = "X-Forwarded-For";
    public static final String HEADER_ORIGIN = "x-origin";

//    --- PARAM ---
    public static final String AUTH_PARAM_CLIENT_IP = "client_ip";
    public static final String AUTH_PARAM_ORIGIN = "origin";

//    --- TOKEN CLAIM ---
    public static final String TOKEN_CLAIM_TYPE = "typ";
    public static final String TOKEN_CLAIM_CLIENT_ID = "cli";
    public static final String TOKEN_CLAIM_SCOPE = "scope";
    public static final String TOKEN_CLAIM_REFRESH_VALUE = "Refresh";
    public static final String TOKEN_CLAIM_USER_ID = "uid";
    public static final String TOKEN_CLAIM_USERNAME = "username";
    public static final String TOKEN_CLAIM_EMAIL = "email";
    public static final String TOKEN_CLAIM_ROLE = "role";
}
