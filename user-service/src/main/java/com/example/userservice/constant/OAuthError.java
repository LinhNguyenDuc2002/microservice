package com.example.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuthError {
    public static final String INVALID_REQUEST = "The request is invalid";
    public static final String NOT_FOUND_REGISTERED_CLIENT = "error.not-found.registered-client";
    public static final String INVALID_GRANT_TYPE = "error.invalid.grant-type";
    public static final String INVALID_CLIENT_ID = "error.invalid.client-id";
    public static final String HEADER_MISSING = "error.header-missing";
    public static final String ACCESS_DENIED = "";
    public static final String INVALID_CLIENT = "";
    public static final String INVALID_CREDENTIALS = "";
    public static final String ACCOUNT_LOCKED = "";
    public static final String SERVER_ERROR = "";
}
