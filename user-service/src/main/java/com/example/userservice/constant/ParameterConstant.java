package com.example.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Define constants related to parameters
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParameterConstant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Page {
        public static final String PAGE = "page";
        public static final String SIZE = "size";

        public static final String DEFAULT_PAGE = "0";
        public static final String DEFAULT_SIZE = "10";
    }
}
