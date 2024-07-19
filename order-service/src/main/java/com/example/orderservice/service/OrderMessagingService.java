package com.example.orderservice.service;

public interface OrderMessagingService {
    void sendMessage(String topic, String message);
}
