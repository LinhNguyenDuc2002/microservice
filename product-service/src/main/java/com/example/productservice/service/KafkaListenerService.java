package com.example.productservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface KafkaListenerService {
    void updateInventory(String request) throws JsonProcessingException;
}
