package com.example.productservice.repository;

import com.example.productservice.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends BaseRepository<Bill, String> {
}
