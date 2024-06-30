package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QBill;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

import java.util.Date;

public class BillPredicate extends BasePredicate {
    private final static QBill qBill = QBill.bill;

    /**
     *
     * @param customer
     * @return
     */
    public BillPredicate customer(String customer) {
        if(!StringUtils.hasText(customer)) {
            criteria.and(qBill.details.any().customer.id.eq(customer));
        }

        return this;
    }

    /**
     *
     * @param startAt
     * @return
     */
    public BillPredicate from(@NotNull Date startAt) {
        criteria.and(qBill.createdDate.after(startAt));

        return this;
    }

    /**
     *
     * @param endAt
     * @return
     */
    public BillPredicate to(@NotNull Date endAt) {
        criteria.and(qBill.createdDate.before(endAt));

        return this;
    }
}
