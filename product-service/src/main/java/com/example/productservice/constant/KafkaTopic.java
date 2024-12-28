package com.example.productservice.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class KafkaTopic {
    public final static String CREATE_CUSTOMER = "create-customer";

    public final static String UPDATE_ORDER_DETAIL = "update-detail";
}
