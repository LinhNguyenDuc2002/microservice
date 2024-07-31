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
     * @param customerId
     * @return
     */
    public BillPredicate withCustomerId(String customerId) {
        if(StringUtils.hasText(customerId)) {
            criteria.and(qBill.details.any().customer.id.eq(customerId));
        }

        return this;
    }

    /**
     *
     * @param status
     * @return
     */
    public BillPredicate withStatus(String status) {
        if(StringUtils.hasText(status) && EnumSet.allOf(BillStatus.class).contains(BillStatus.valueOf(status))) {
            criteria.and(qBill.status.eq(BillStatus.valueOf(status)));
        }

        return this;
    }

    /**
     *
     * @param shopId
     * @return
     */
    public BillPredicate withShopId(String shopId) {
        if(StringUtils.hasText(shopId)) {
            criteria.and(qBill.shopId.eq(shopId));
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
