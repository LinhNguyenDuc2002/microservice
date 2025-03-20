package com.example.productservice.repository;

import com.example.productservice.entity.ProductAttribute;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeRepository extends BaseRepository<ProductAttribute, ProductAttribute.ProductAttributeId> {
}
