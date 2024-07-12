package com.example.userservice.service.impl;

import com.example.userservice.service.OrderMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderMessagingServiceImpl implements OrderMessagingService {
    @Value("${spring.kafka.producer.topics[0].name}")
    private String topic1;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void sendEmail(String emailMessage) {
        kafkaTemplate.sendDefault(emailMessage);
    }

    @Override
    public void createCustomer(String customerMessage) {
        kafkaTemplate.send(topic1, customerMessage);
    }
}
