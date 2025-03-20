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
     * @param id
     * @return
     */
    public ProductTypePredicate withProductId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qProductType.product.id.eq(id));
        }

        return this;
    }

    /**
     * @param ids
     * @return
     */
    public ProductTypePredicate inIds(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            criteria.and(qProductType.id.in(ids));
        }

        return this;
    }

    public ProductTypePredicate withQuantityGreater(int quantity) {
        criteria.and(qProductType.quantity.goe(quantity));

        return this;
    }
}
