package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QProduct;
import org.springframework.util.StringUtils;

import java.util.List;

public class ProductPredicate extends BasePredicate {
    private final static QProduct qProduct = QProduct.product;

    /**
     *
     * @param key
     * @return
     */
    public ProductPredicate search(String key) {
        if(StringUtils.hasText(key)) {
           criteria.and(qProduct.name.contains(key)) ;
        }

        return this;
    }

    /**
     *
     * @param ids
     * @return
     */
    public ProductPredicate withIds(List<String> ids) {
        if(ids != null && !ids.isEmpty()) {
            criteria.and(qProduct.id.in(ids)) ;
        }

        return this;
    }

    /**
     *
     * @param id
     * @return
     */
    public ProductPredicate category(String id) {
        if(StringUtils.hasText(id)) {
            criteria.and(qProduct.category.id.eq(id)) ;
        }

        return this;
    }

    /**
     *
     * @param id
     * @return
     */
    public ProductPredicate shop(String id) {
        if(StringUtils.hasText(id)) {
            criteria.and(qProduct.shop.id.eq(id)) ;
        }

        return this;
    }
}
