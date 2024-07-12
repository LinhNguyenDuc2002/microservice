package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QCustomer;
import org.springframework.util.StringUtils;

public class CustomerPredicate extends BasePredicate {
    private final static QCustomer qCustomer = QCustomer.customer;

    /**
     *
     * @param id
     * @return
     */
    public CustomerPredicate withAccountId(String id) {
        if(StringUtils.hasText(id)) {
            criteria.and(qCustomer.accountId.eq(id));
        }

        return this;
    }
}
