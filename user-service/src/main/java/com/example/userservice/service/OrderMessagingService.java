package com.example.userservice.service;

import com.example.userservice.message.email.EmailMessage;

public interface OrderMessagingService {
    void sendEmail(EmailMessage emailMessage);
}
