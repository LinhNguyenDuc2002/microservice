package com.example.orderservice.repository.predicate;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.entity.QPurchaseOrder;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.EnumSet;

public class OrderPredicate extends BasePredicate {
    private final static QPurchaseOrder qPurchaseOrder = QPurchaseOrder.purchaseOrder;

    /**
     * @param customerId
     * @return
     */
    public OrderPredicate withCustomerId(String customerId) {
        if (StringUtils.hasText(customerId)) {
            criteria.and(qPurchaseOrder.orderDetails.any().customerId.eq(customerId));
        }

        return this;
    }

    /**
     * @param status
     * @return
     */
    public OrderPredicate withStatus(String status) {
        if (StringUtils.hasText(status) && EnumSet.allOf(OrderStatus.class).contains(OrderStatus.valueOf(status))) {
            criteria.and(qPurchaseOrder.status.eq(OrderStatus.valueOf(status)));
        }

        return this;
    }

    /**
     * @param startAt
     * @return
     */
    public OrderPredicate from(@NotNull Date startAt) {
        criteria.and(qPurchaseOrder.createdDate.after(startAt));

        return this;
    }

    /**
     * @param endAt
     * @return
     */
    public OrderPredicate to(@NotNull Date endAt) {
        criteria.and(qPurchaseOrder.createdDate.before(endAt));

        return this;
    }
}
