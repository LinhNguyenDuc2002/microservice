package com.example.orderservice.repository;

import com.example.orderservice.entity.OrderDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends BaseRepository<OrderDetail, String> {
}
