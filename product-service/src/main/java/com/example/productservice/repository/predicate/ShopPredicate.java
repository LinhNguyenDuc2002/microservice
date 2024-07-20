package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QShop;
import org.springframework.util.StringUtils;

public class ShopPredicate extends BasePredicate {
    private final static QShop qShop = QShop.shop;

    /**
     *
     * @param customerId
     * @return
     */
    public ShopPredicate withCustomerId(String customerId) {
        if(StringUtils.hasText(customerId)) {
            criteria.and(qShop.customer.id.eq(customerId));
        }

        return this;
    }

    /**
     *
     * @param accountId
     * @return
     */
    public ShopPredicate withAccountId(String accountId) {
        if(StringUtils.hasText(accountId)) {
            criteria.and(qShop.customer.accountId.eq(accountId));
        }

        return this;
    }
}
