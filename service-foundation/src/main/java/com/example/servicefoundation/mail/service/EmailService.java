package com.example.servicefoundation.mail.service;

import com.example.servicefoundation.mail.message.BaseMessage;
import com.example.servicefoundation.mail.message.EmailMessage;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Handle sending email
 *
 * @param <T>
 */
public interface EmailService<T extends BaseMessage> {
    void send(EmailMessage emailMessage);
}
