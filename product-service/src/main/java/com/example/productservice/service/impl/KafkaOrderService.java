package com.example.productservice.service.impl;

import com.example.productservice.exception.InvalidException;
import com.example.productservice.payload.CustomerRequest;
import com.example.productservice.service.CustomerService;
import com.example.productservice.service.OrderMessagingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class KafkaOrderService implements OrderMessagingService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void updateUnitPrice(Map<String, Double> productList) throws JsonProcessingException {
        kafkaTemplate.sendDefault(mapper.writeValueAsString(productList));
    }

    @KafkaListener(topics = "create-customer")
    @Override
    public void createCustomer(String customerRequest) throws JsonProcessingException, InvalidException {
        CustomerRequest customer = mapper.readValue(customerRequest, CustomerRequest.class);
        customerService.create(customer);
    }
}
