package com.example.productservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18nMessage {
    /**
     * Level: INFO
     */
    public final static String INFO_CREATE_COMMENT = "info.create-comment";
    public final static String INFO_UPDATE_COMMENT = "info.update-comment";
    public final static String INFO_DELETE_COMMENT = "info.delete-comment";
    public final static String INFO_GET_COMMENT = "info.get-comment";
    public final static String INFO_CREATE_CATEGORY = "info.create-category";
    public final static String INFO_UPDATE_CATEGORY = "info.update-category";
    public final static String INFO_GET_CATEGORY = "info.get-category";
    public final static String INFO_DELETE_CATEGORY = "info.delete-category";
    public final static String INFO_CREATE_PRODUCT = "info.create-product";
    public final static String INFO_UPDATE_PRODUCT = "info.update-product";
    public final static String INFO_GET_ALL_PRODUCT = "info.get-all-product";
    public final static String INFO_GET_PRODUCT = "info.get-product";
    public final static String INFO_DELETE_PRODUCT = "info.delete-product";
    public final static String INFO_ADD_PRODUCT_DETAIL = "info.add-product-detail";
    public final static String INFO_GET_ALL_PRODUCT_DETAIL = "info.get-all-product-detail";
    public final static String INFO_GET_PRODUCT_DETAIL = "info.get-product-detail";
    public final static String INFO_UPDATE_PRODUCT_DETAIL = "info.update-product-detail";
    public final static String INFO_DELETE_PRODUCT_DETAIL = "info.delete-product-detail";

    /**
     * Level: WARN
     */
    public final static String WARN_PRODUCT_DETAIL_SOLD_OUT = "info.product-detail.sold-out";

    /**
     * Level: ERROR
     */
    public final static String ERROR_DATA_INVALID = "error.data.invalid";
    public final static String ERROR_PRODUCT_NAME_EXISTED = "error.product-name.existed";
    public final static String ERROR_PRODUCT_NOT_FOUND = "error.product.not-found";
    public final static String ERROR_PRODUCT_SOLD_OUT = "error.product.sold-out";
    public final static String ERROR_CATEGORY_NOT_FOUND = "error.category.not-found";
    public final static String ERROR_CUSTOMER_NOT_FOUND = "";
    public final static String ERROR_SHOP_NOT_FOUND = "";
    public final static String ERROR_SHOP_EXIST = "";
    public final static String ERROR_COMMENT_NOT_FOUND = "error.comment.not-found";
    public final static String ERROR_COMMENT_EDIT = "error.category.edit";
    public final static String ERROR_TOKEN_NOT_FOUND = "error.token.not-found";
    public final static String ERROR_PRODUCT_DETAIL_NOT_FOUND = "error.product-detail.not-found";
    public final static String ERROR_PRODUCT_DETAIL_ONLY_ONE = "";
    public final static String ERROR_PRODUCT_QUANTITY_NOT_ENOUGH = "error.product-quantity.not-enough";
    public final static String ERROR_IMAGE_NOT_FOUND = "error.image.not-found";
    public final static String ERROR_PRODUCT_DETAIL_NAME_DUPLICATED = "error.product-detail-name.duplicated";
    public final static String ERROR_IMAGE_NUMBER_EXCEEDED = "error.image-number.exceeded";
}
