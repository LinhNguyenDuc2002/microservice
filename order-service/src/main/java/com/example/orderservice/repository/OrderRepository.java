package com.example.orderservice.repository;

import com.example.orderservice.entity.PurchaseOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<PurchaseOrder, String> {
}
