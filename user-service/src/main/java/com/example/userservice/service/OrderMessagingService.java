package com.example.userservice.service;

public interface OrderMessagingService {
    void sendEmail(String emailMessage);

    void createCustomer(String customerMessage);
}
