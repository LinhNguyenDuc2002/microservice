package com.example.orderservice.repository.predicate;

import com.example.orderservice.constant.OrderDetailStatus;
import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.entity.QOrderDetail;
import org.springframework.util.StringUtils;

import java.util.EnumSet;
import java.util.List;

public class OrderDetailPredicate extends BasePredicate {
    private final static QOrderDetail qOrderDetail = QOrderDetail.orderDetail;

    /**
     * @param productIds
     * @return
     */
    public OrderDetailPredicate withProductDetailIds(List<String> productIds) {
        if (productIds != null && !productIds.isEmpty()) {
            criteria.and(qOrderDetail.productDetailId.in(productIds));
        }

        return this;
    }

    /**
     * @param productId
     * @return
     */
    public OrderDetailPredicate withProductDetailId(String productId) {
        if (StringUtils.hasText(productId)) {
            criteria.and(qOrderDetail.productDetailId.eq(productId));
        }

        return this;
    }

    /**
     * @param id
     * @return
     */
    public OrderDetailPredicate withId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qOrderDetail.id.eq(id));
        }

        return this;
    }

    /**
     * @param customerId
     * @return
     */
    public OrderDetailPredicate withCustomerId(String customerId) {
        if (StringUtils.hasText(customerId)) {
            criteria.and(qOrderDetail.customerId.eq(customerId));
        }

        return this;
    }

    /**
     * @param status
     * @return
     */
    public OrderDetailPredicate withCommentStatus(Boolean status) {
        if (status != null) {
            criteria.and(status ? qOrderDetail.commentStatus.eq(true) : qOrderDetail.commentStatus.eq(false));
        }

        return this;
    }

    /**
     * @param status
     * @return
     */
    public OrderDetailPredicate withStatus(OrderDetailStatus status) {
        if (status != null && EnumSet.allOf(OrderDetailStatus.class).contains(status)) {
            criteria.and(qOrderDetail.status.eq(status));
        }

        return this;
    }

    /**
     * @param status
     * @return
     */
    public OrderDetailPredicate withoutStatus(OrderDetailStatus status) {
        if (status != null && EnumSet.allOf(OrderDetailStatus.class).contains(status)) {
            criteria.and(qOrderDetail.status.ne(status));
        }

        return this;
    }

    /**
     * @param orderStatus
     * @return
     */
    public OrderDetailPredicate withBillStatus(OrderStatus orderStatus) {
        if (orderStatus != null && EnumSet.allOf(OrderStatus.class).contains(orderStatus)) {
            criteria.and(qOrderDetail.order.status.eq(orderStatus));
        }

        return this;
    }
}
