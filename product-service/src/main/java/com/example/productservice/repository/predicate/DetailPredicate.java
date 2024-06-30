package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QDetail;

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
}
