package com.example.productservice.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class KafkaTopic {
    public final static String UPDATE_ORDER_DETAIL = "update-detail";

    public final static String INVENTORY_DEDUCT = "inventory.deduct";

    public final static String ORDER_UPDATE = "order.update";
}
