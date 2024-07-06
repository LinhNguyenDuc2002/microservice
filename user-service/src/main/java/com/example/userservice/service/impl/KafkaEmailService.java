package com.example.userservice.service.impl;

import com.example.userservice.service.OrderMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEmailService implements OrderMessagingService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void sendEmail(String emailMessage) {
        kafkaTemplate.sendDefault(emailMessage);
    }
}
