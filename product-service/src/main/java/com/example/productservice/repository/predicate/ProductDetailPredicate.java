package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QProductDetail;
import org.springframework.util.StringUtils;

import java.util.List;

public class ProductDetailPredicate extends BasePredicate {
    private final static QProductDetail qProductDetail = QProductDetail.productDetail;

    /**
     * @param id
     * @return
     */
    public ProductDetailPredicate withId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qProductDetail.id.eq(id));
        }

        return this;
    }

    /**
     * @param search
     * @return
     */
    public ProductDetailPredicate search(String search) {
        if (StringUtils.hasText(search)) {
            criteria.and(qProductDetail.name.contains(search));
        }

        return this;
    }

    /**
     * @param id
     * @return
     */
    public ProductDetailPredicate withProductId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qProductDetail.product.id.eq(id));
        }

        return this;
    }

    /**
     * @param ids
     * @return
     */
    public ProductDetailPredicate inIds(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            criteria.and(qProductDetail.id.in(ids));
        }

        return this;
    }

    public ProductDetailPredicate withStatus(boolean status) {
        criteria.and(qProductDetail.status.eq(status));

        return this;
    }
}
