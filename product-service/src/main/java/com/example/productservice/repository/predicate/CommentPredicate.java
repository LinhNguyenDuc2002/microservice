package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QComment;
import org.springframework.util.StringUtils;

public class CommentPredicate extends BasePredicate {
    private final static QComment qComment = QComment.comment;

    /**
     *
     * @param accountId
     * @return
     */
    public CommentPredicate withAccountId(String accountId) {
        if(StringUtils.hasText(accountId)) {
            criteria.and(qComment.customer.accountId.eq(accountId));
        }

        return this;
    }
}
