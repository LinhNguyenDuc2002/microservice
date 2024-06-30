package com.example.productservice.repository;

import com.example.productservice.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRepository extends BaseRepository<Detail, String> {
    @Query("SELECT SUM(d.unitPrice*d.quantity) FROM Detail d WHERE d.product.id = :id AND d.status = true")
    Double getSales(String id);
}
