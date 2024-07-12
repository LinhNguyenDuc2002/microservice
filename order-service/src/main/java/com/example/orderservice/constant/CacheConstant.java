package com.example.orderservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheConstant {
    public final static String PREFIX_CACHE = "product-service";

    public final static String KEY_PRODUCT_CACHE = PREFIX_CACHE + ":products:%d:%d:%s:%s";
}
