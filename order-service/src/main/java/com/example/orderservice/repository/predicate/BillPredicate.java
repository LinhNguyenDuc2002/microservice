package com.example.orderservice.repository.predicate;

import com.example.orderservice.constant.BillStatus;
import com.example.orderservice.entity.QBill;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.EnumSet;

public class BillPredicate extends BasePredicate {
    private final static QBill qBill = QBill.bill;

    /**
     *
     * @param customer
     * @return
     */
    public BillPredicate customer(String customer) {
        if(StringUtils.hasText(customer)) {
            criteria.and(qBill.details.any().customer.id.eq(customer));
        }

        return this;
    }

    /**
     *
     * @param status
     * @return
     */
    public BillPredicate status(String status) {
        if(StringUtils.hasText(status) && EnumSet.allOf(BillStatus.class).contains(BillStatus.valueOf(status))) {
            criteria.and(qBill.status.eq(BillStatus.valueOf(status)));
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
