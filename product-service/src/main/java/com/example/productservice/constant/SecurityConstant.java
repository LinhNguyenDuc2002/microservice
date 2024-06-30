package com.example.productservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstant {
    public final static String TOKEN_CLAIM_TYPE = "typ";

    public final static String TOKEN_CLAIM_ROLE = "role";
}
