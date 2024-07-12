package com.example.productservice.service;

import com.example.productservice.exception.InvalidException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface OrderMessagingService {
    void updateUnitPrice(Map<String, Double> productList) throws JsonProcessingException;

    void createCustomer(String customerRequest) throws JsonProcessingException, InvalidException;
}
