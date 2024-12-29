package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QImage;
import org.springframework.util.StringUtils;

public class ImagePredicate extends BasePredicate {
    private final static QImage qImage = QImage.image;

    /**
     * @param id
     * @return
     */
    public ImagePredicate withProductId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qImage.product.id.eq(id));
        }

        return this;
    }

    /**
     * @param id
     * @return
     */
    public ImagePredicate withCommentId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qImage.comment.id.eq(id));
        }

        return this;
    }
}
