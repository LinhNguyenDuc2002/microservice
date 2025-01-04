package com.example.orderservice.repository.predicate;

import com.example.orderservice.entity.QReceiver;
import org.springframework.util.StringUtils;

public class ReceiverPredicate extends BasePredicate {
    private final static QReceiver qReceiver = QReceiver.receiver;

    /**
     * @param id
     * @return
     */
    public ReceiverPredicate withId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qReceiver.id.eq(id));
        }

        return this;
    }

    /**
     * @param status
     * @return
     */
    public ReceiverPredicate withStatus(boolean status) {
        criteria.and(qReceiver.status.eq(status));

        return this;
    }

    /**
     * @param id
     * @return
     */
    public ReceiverPredicate withAccountId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qReceiver.accountId.eq(id));
        }

        return this;
    }

    /**
     * @param name
     * @param phoneNumber
     * @return
     */
    public ReceiverPredicate withNameOrPhoneNumber(String name, String phoneNumber) {
        if (StringUtils.hasText(name)) {
            criteria.and(qReceiver.name.eq(name).or(qReceiver.phoneNumber.eq(phoneNumber)));
        }

        return this;
    }

}
