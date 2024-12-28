package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QComment;
import org.springframework.util.StringUtils;

public class CommentPredicate extends BasePredicate {
    private final static QComment qComment = QComment.comment;

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
}
