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

    /**
     *
     * @param productId
     * @return
     */
    public CommentPredicate withProductId(String productId) {
        if(StringUtils.hasText(productId)) {
            criteria.and(qComment.product.id.eq(productId));
        }

        return this;
    }

    /**
     *
     * @param shopId
     * @return
     */
    public CommentPredicate withShopId(String shopId) {
        if(StringUtils.hasText(shopId)) {
            criteria.and(qComment.product.shop.id.eq(shopId));
        }

        return this;
    }
}
