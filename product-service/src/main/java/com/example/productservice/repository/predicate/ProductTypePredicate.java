package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QProductType;
import org.springframework.util.StringUtils;

import java.util.List;

public class ProductTypePredicate extends BasePredicate {
    private final static QProductType qProductType = QProductType.productType;

    /**
     * @param id
     * @return
     */
    public ProductTypePredicate withId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qProductType.id.eq(id));
        }

        return this;
    }

    /**
     * @param productId
     * @return
     */
    public ProductTypePredicate withProductId(String productId) {
        if (StringUtils.hasText(productId)) {
            criteria.and(qProductType.product.id.eq(productId));
        }

        return this;
    }
}
