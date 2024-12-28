package com.example.orderservice.repository.predicate;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.entity.QOrder;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.EnumSet;

public class OrderPredicate extends BasePredicate {
    private final static QOrder qOrder = QOrder.order;

    /**
     * @param customerId
     * @return
     */
    public OrderPredicate withCustomerId(String customerId) {
        if (StringUtils.hasText(customerId)) {
            criteria.and(qOrder.orderDetails.any().customerId.eq(customerId));
        }

        return this;
    }

    /**
     * @param status
     * @return
     */
    public OrderPredicate withStatus(String status) {
        if (StringUtils.hasText(status) && EnumSet.allOf(OrderStatus.class).contains(OrderStatus.valueOf(status))) {
            criteria.and(qOrder.status.eq(OrderStatus.valueOf(status)));
        }

        return this;
    }

    /**
     * @param startAt
     * @return
     */
    public OrderPredicate from(@NotNull Date startAt) {
        criteria.and(qOrder.createdDate.after(startAt));

        return this;
    }

    /**
     * @param endAt
     * @return
     */
    public OrderPredicate to(@NotNull Date endAt) {
        criteria.and(qOrder.createdDate.before(endAt));

        return this;
    }
}
