package com.example.productservice.repository;

import com.example.productservice.entity.ProductDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends BaseRepository<ProductDetail, String> {
}
