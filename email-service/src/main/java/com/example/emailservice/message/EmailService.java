package com.example.emailservice.message;

import com.example.emailservice.message.email.EmailMessage;

/**
 * Handle sending email
 * @param <T>
 */
public interface EmailService<T extends BaseMessage> {
    void sendMessage(EmailMessage message);

    void sendMessage(String message);
}
