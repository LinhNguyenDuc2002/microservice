package com.example.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KafkaTopic {
    public final static String SEND_EMAIL = "send-email";

    public final static String CREATE_CUSTOMER = "create-customer";
}
