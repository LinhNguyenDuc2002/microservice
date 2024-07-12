package com.example.orderservice.repository.predicate;

import com.example.orderservice.entity.QDetail;

import java.util.List;

public class DetailPredicate extends BasePredicate {
    private final static QDetail qDetail = QDetail.detail;

    /**
     *
     * @param status
     * @return
     */
    public DetailPredicate status(Boolean status) {
        if(status != null) {
            criteria.and(status?qDetail.status.eq(true):qDetail.status.eq(false));
        }

        return this;
    }

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
}
