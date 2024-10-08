package com.example.orderservice.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParameterConstant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Page {
        public static final String PAGE = "page";
        public static final String SIZE = "size";

        public static final String DEFAULT_PAGE = "0";
        public static final String DEFAULT_SIZE = "10";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Quantity {
        public static final String DEFAULT_MIN_QUANTITY = "1";
    }
}
