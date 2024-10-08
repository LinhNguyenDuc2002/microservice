package com.example.emailservice.message;

import com.example.emailservice.message.email.EmailMessage;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Handle sending email
 * @param <T>
 */
public interface EmailService<T extends BaseMessage> {
    void sendMessage(String payload) throws JsonProcessingException;
}
