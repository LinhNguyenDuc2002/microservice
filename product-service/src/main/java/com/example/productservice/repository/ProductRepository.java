package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product, String> {
}
