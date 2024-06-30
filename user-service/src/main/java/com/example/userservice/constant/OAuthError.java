package com.example.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuthError {
    public static final String INVALID_REQUEST = "The request is invalid";
    public static final String INVALID_GRANT_TYPE = "Invalid grant type";
    public static final String HEADER_MISSING = "";
    public static final String ACCESS_DENIED = "";
    public static final String INVALID_CLIENT = "";
    public static final String INVALID_CREDENTIALS = "";
    public static final String ACCOUNT_LOCKED = "";
    public static final String SERVER_ERROR = "";
}
