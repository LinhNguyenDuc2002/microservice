package com.example.orderservice.service.impl;

import com.example.orderservice.constant.KafkaTopic;
import com.example.orderservice.entity.Detail;
import com.example.orderservice.exception.InvalidException;
import com.example.orderservice.payload.CustomerRequest;
import com.example.orderservice.repository.DetailRepository;
import com.example.orderservice.repository.predicate.DetailPredicate;
import com.example.orderservice.service.CustomerService;
import com.example.orderservice.service.KafkaListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class KafkaListenerServiceImpl implements KafkaListenerService {
    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ObjectMapper mapper;

    @KafkaListener(topics = KafkaTopic.UPDATE_UNIT_PRICE_DETAIL)
    @Override
    public void updateUnitPrice(String products) throws JsonProcessingException {
        Map<String, Double> productList = mapper.readValue(products, new TypeReference<Map<String, Double>>() {});
        Set<String> keyProduct = productList.keySet();

        DetailPredicate detailPredicate = new DetailPredicate()
                .withProductIds(keyProduct.stream().toList())
                .withStatus(false);
        List<Detail> details = detailRepository.findAll(detailPredicate.getCriteria());

        for(Detail detail : details) {
            detail.setUnitPrice(productList.get(detail.getProduct()));
        }

        detailRepository.saveAll(details);
    }

    @KafkaListener(topics = KafkaTopic.CREATE_CUSTOMER)
    @Override
    public void createCustomer(String customerRequest) throws JsonProcessingException, InvalidException {
        CustomerRequest customer = mapper.readValue(customerRequest, CustomerRequest.class);
        customerService.create(customer);
    }
}
