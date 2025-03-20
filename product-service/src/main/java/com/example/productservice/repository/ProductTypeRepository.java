package com.example.productservice.repository;

import com.example.productservice.entity.ProductType;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends BaseRepository<ProductType, String> {
}
