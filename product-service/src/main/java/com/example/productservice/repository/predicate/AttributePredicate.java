package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QAttribute;
import org.springframework.util.StringUtils;

public class AttributePredicate extends BasePredicate {
    private final static QAttribute qAttribute = QAttribute.attribute;

    /**
     * @param id
     * @param featureId
     * @return
     */
    public AttributePredicate withIdAndFeatureId(String id, String featureId) {
        if (StringUtils.hasText(id) && StringUtils.hasText(featureId)) {
            criteria.and(qAttribute.id.eq(id).and(qAttribute.feature.id.eq(featureId)));
        }

        return this;
    }
}
