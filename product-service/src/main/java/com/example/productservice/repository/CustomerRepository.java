package com.example.productservice.repository;

import com.example.productservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, String> {
    Optional<Customer> findByAccountId(String accountId);
}
