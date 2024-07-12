package com.example.orderservice.repository;

import com.example.orderservice.entity.Detail;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRepository extends BaseRepository<Detail, String> {
}
