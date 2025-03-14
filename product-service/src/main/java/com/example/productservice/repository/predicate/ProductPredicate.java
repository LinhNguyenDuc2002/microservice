package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QProduct;
import org.springframework.util.StringUtils;

import java.util.List;

public class ProductPredicate extends BasePredicate {
    private final static QProduct qProduct = QProduct.product;

    /**
     * @param key
     * @return
     */
    public ProductPredicate search(String key) {
        if (StringUtils.hasText(key)) {
            criteria.and(qProduct.name.contains(key));
        }

        return this;
    }

    /**
     * @param productIds
     * @return
     */
    public ProductPredicate inProductIds(List<String> productIds) {
        if (productIds != null && !productIds.isEmpty()) {
            criteria.and(qProduct.id.in(productIds));
        }

        return this;
    }

    /**
     * @param categoryId
     * @return
     */
    public ProductPredicate withCategoryId(String categoryId) {
        if (StringUtils.hasText(categoryId)) {
            criteria.and(qProduct.category.id.eq(categoryId));
        }

        return this;
    }

    /**
     * @param name
     * @return
     */
    public ProductPredicate withName(String name) {
        if (StringUtils.hasText(name)) {
            criteria.and(qProduct.name.eq(name));
        }

        return this;
    }
}
