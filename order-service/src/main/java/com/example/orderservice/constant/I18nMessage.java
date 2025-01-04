package com.example.orderservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18nMessage {
    /**
     * INFO
     */
    public final static String INFO_CREATE_ORDER = "info.create-order";
    public final static String INFO_GET_ORDER = "info.get-order";
    public final static String INFO_DELETE_ORDER = "info.delete-order";
    public final static String INFO_CHANGE_ORDER_STATUS = "info.change-order-status";
    public final static String INFO_UPDATE_DELIVERY_INFO = "info.update-delivery-info";
    public final static String INFO_ADD_ORDER_DETAIL = "info.add-order-detail";
    public final static String INFO_UPDATE_ORDER_DETAIL = "info.update-order-detail";
    public final static String INFO_GET_ORDER_DETAIL = "info.get-order-detail";
    public final static String INFO_DELETE_ORDER_DETAIL = "info.delete-order-detail";
    public final static String INFO_ADD_RECEIVER = "info.add-receiver";
    public final static String INFO_DELETE_RECEIVER = "info.delete-receiver";
    public final static String INFO_UPDATE_RECEIVER = "info.update-receiver";
    public final static String INFO_GET_RECEIVER = "info.get-receiver";

    /**
     * WARN
     */


    /**
     * ERROR
     */
    public final static String ERROR_ORDER_DETAIL_NOT_FOUND = "error.order-detail.not-found";
    public final static String ERROR_ORDER_DETAIL_CAN_NOT_DELETE = "error.order-detail.can-not-delete";
    public final static String ERROR_DATA_INVALID = "error.data.invalid";
    public final static String ERROR_UNAUTHORIZED = "error.unauthorized";
    public final static String ERROR_PRODUCT_NOT_FOUND = "error.product.not-found";
    public final static String ERROR_ORDER_NOT_FOUND = "error.order.not-found";
    public final static String ERROR_ORDER_CAN_NOT_DELETE = "error.order.can-not-delete";
    public final static String ERROR_ORDER_PROCESSED = "error.order.processed";
    public final static String ERROR_RECEIVER_NOT_FOUND = "error.receiver.not-found";
    public final static String ERROR_STATUS_INVALID = "error.status.invalid";
    public final static String ERROR_QUANTITY_MIN = "error.quantity.min";
    public final static String ERROR_RECEIVER_INFO_EXIST = "error.receiver-info.exist";
    public final static String ERROR_PHONE_NUMBER_EXIST = "error.phone-number.exist";
    public final static String ERROR_TOKEN_NOT_FOUND = "error.token.not-found";
}
