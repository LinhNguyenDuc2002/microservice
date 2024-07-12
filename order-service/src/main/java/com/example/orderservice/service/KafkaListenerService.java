package com.example.orderservice.service;

import com.example.orderservice.exception.InvalidException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface KafkaListenerService {
    void updateUnitPrice(String products) throws JsonProcessingException;

    void createCustomer(String customerRequest) throws JsonProcessingException, InvalidException;
}
