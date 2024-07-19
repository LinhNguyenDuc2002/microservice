package com.example.orderservice.message.email;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailConstant {
    public final static String TEMPLATE_EMAIL_CONFIRM_ORDER = "confirm-order.html";

    public final static String ARG_LOGO_URI = "ARG_LOGO_URI";
    public final static String ARG_BILL_ID = "ARG_BILL_ID";
    public final static String ARG_RECEIVER_NAME = "ARG_RECEIVER_NAME";
    public final static String ARG_RECEIVER_PHONE = "ARG_RECEIVER_PHONE";
    public final static String ARG_DELIVERY_ADDRESS = "ARG_DELIVERY_ADDRESS";
    public final static String ARG_SUPPORT_EMAIL = "ARG_SUPPORT_EMAIL";
    public final static String ARG_VERIFY_EMAIL_SUBJECT = "[Blue shop] Verify account";
    public final static String ARG_CONFIRM_ORDER_SUBJECT = "[Blue shop] Confirm order";
}
