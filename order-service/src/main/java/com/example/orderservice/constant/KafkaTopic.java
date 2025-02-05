package com.example.orderservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaTopic {
    public final static String CREATE_CUSTOMER = "create-customer";

    public final static String UPDATE_UNIT_PRICE_DETAIL = "update-detail";
}
