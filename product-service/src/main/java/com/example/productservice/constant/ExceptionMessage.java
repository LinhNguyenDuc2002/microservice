package com.example.productservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessage {
    public final static String ERROR_PRODUCT_INVALID_INPUT = "The request is invalid";
    public final static String ERROR_PRODUCT_NOT_FOUND = "The product does not exist";

    public final static String ERROR_CATEGORY_NOT_FOUND = "The request is invalid";

    public final static String ERROR_CUSTOMER_INVALID_INPUT = "";
    public final static String ERROR_CUSTOMER_NOT_FOUND = "";

    public final static String ERROR_SHOP_NOT_FOUND = "";

    public final static String ERROR_DETAIL_NOT_FOUND = "";

    public final static String ERROR_COMMENT_NOT_FOUND = "";
}
