package com.example.orderservice.repository;

import com.example.orderservice.entity.Bill;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends BaseRepository<Bill, String> {
}
