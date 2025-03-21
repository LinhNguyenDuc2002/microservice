package com.example.productservice.repository;

import com.example.productservice.entity.ProductFeature;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeRepository extends BaseRepository<ProductFeature, ProductFeature.ProductFeatureId> {
}
