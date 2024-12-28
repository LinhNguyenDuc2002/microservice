package com.example.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface KafkaListenerService {
    void updateOrderDetail(String products) throws JsonProcessingException;
}
