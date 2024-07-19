package com.example.userservice.service;

public interface OrderMessagingService {
    void sendMessage(String topic, String emailMessage);
}
