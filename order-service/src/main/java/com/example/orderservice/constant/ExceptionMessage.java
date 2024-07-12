package com.example.orderservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessage {
    public final static String ERROR_PRODUCT_INVALID_INPUT = "The request is invalid";

    public final static String ERROR_DETAIL_NOT_FOUND = "The detail is not found";
    public final static String ERROR_DETAIL_INVALID_INPUT = "The request is invalid";

    public final static String ERROR_CUSTOMER_INVALID_INPUT = "";
    public final static String ERROR_CUSTOMER_NOT_FOUND = "";
}
