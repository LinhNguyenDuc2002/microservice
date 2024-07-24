package com.example.orderservice.repository.predicate;

import com.example.orderservice.constant.BillStatus;
import com.example.orderservice.entity.QDetail;
import org.springframework.util.StringUtils;

import java.util.EnumSet;
import java.util.List;

public class DetailPredicate extends BasePredicate {
    private final static QDetail qDetail = QDetail.detail;

    /**
     *
     * @param productIds
     * @return
     */
    public DetailPredicate withProductIds(List<String> productIds) {
        if(productIds != null && !productIds.isEmpty()) {
            criteria.and(qDetail.product.in(productIds));
        }

        return this;
    }

    /**
     *
     * @param id
     * @return
     */
    public DetailPredicate withId(String id) {
        if(StringUtils.hasText(id)) {
            criteria.and(qDetail.id.eq(id));
        }

        return this;
    }

    /**
     *
     * @param customerId
     * @return
     */
    public DetailPredicate withCustomerId(String customerId) {
        if(StringUtils.hasText(customerId)) {
            criteria.and(qDetail.customer.id.eq(customerId));
        }

        return this;
    }

    /**
     *
     * @param status
     * @return
     */
    public DetailPredicate withCommentStatus(Boolean status) {
        if(status != null) {
            criteria.and(status?qDetail.commentStatus.eq(true):qDetail.commentStatus.eq(false));
        }

        return this;
    }

    /**
     *
     * @param status
     * @return
     */
    public DetailPredicate withStatus(Boolean status) {
        if(status != null) {
            criteria.and(status?qDetail.status.eq(true):qDetail.status.eq(false));
        }

        return this;
    }

    /**
     *
     * @param billStatus
     * @return
     */
    public DetailPredicate withBillStatus(BillStatus billStatus) {
        if(billStatus != null && EnumSet.allOf(BillStatus.class).contains(billStatus)) {
            criteria.and(qDetail.bill.status.eq(billStatus));
        }

        return this;
    }
}
