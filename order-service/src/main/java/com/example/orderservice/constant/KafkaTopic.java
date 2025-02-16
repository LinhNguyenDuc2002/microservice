package com.example.orderservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaTopic {
    public final static String UPDATE_UNIT_PRICE_DETAIL = "update-detail";

    public final static String INVENTORY_DEDUCT = "inventory.deduct";

    public final static String ORDER_UPDATE = "order.update";
}
