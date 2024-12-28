package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order, String> {
}
