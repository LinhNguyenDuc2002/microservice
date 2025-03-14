package com.example.productservice.repository;

import com.example.productservice.entity.Shop;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends BaseRepository<Shop, String> {
}
