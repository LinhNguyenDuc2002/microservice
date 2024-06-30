package com.example.productservice.repository.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class BasePredicate {
    protected final BooleanBuilder criteria = new BooleanBuilder();

    /**
     * Get the combination of query criteria
     *
     * @return
     */
    public Predicate getCriteria() {
        return criteria;
    }
}
