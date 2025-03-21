package com.example.productservice.repository;

import com.example.productservice.entity.Feature;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends BaseRepository<Feature, String> {
    /**
     * SELECT DISTINCT f.* FROM product_service.feature f
     * JOIN product_service.attribute a ON a.feature_id = f.id
     * JOIN product_service.product_attribute pa ON pa.attribute_id = a.id
     * JOIN product_service.product_type pt ON pt.id = pa.product_type_id
     * WHERE pt.product_id = '1161b5eb-fe02-40a1-8c34-3028417d5e70';
     * @param productId
     * @return
     */
    @Query("SELECT DISTINCT f, pf.level FROM Feature f " +
            "JOIN f.productFeatures pf " +
            "WHERE pf.product.id = :productId " +
            "ORDER BY pf.level")
    List<Feature> findByProductId(String productId);
}
