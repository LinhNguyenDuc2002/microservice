package com.example.productservice.repository.predicate;

import com.example.productservice.entity.QCategory;
import org.springframework.util.StringUtils;

public class CategoryPredicate extends BasePredicate {
    private final static QCategory qCategory = QCategory.category;

    /**
     * @param key
     * @return
     */
    public CategoryPredicate search(String key) {
        if (StringUtils.hasText(key)) {
            criteria.and(qCategory.name.contains(key));
        }

        return this;
    }
}
