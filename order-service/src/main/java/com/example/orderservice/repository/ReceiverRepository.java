package com.example.orderservice.repository;

import com.example.orderservice.entity.Receiver;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverRepository extends BaseRepository<Receiver, String> {
    boolean existsByName(String name);

    boolean existsByPhoneNumber(String phone);
}
