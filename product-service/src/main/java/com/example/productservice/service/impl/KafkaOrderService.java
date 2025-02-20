package com.example.productservice.service.impl;

import com.example.productservice.constant.KafkaTopic;
import com.example.productservice.payload.message.OrderMessageResponse;
import com.example.productservice.payload.orderservice.request.UpdateOrderDetailReq;
import com.example.productservice.service.OrderMessagingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaOrderService implements OrderMessagingService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void updateOrderDetail(UpdateOrderDetailReq request) throws JsonProcessingException {
        kafkaTemplate.send(KafkaTopic.UPDATE_ORDER_DETAIL, mapper.writeValueAsString(request));
    }

    @Override
    public void updateOrderStatus(OrderMessageResponse orderMessageResponse) throws JsonProcessingException {
        kafkaTemplate.send(KafkaTopic.ORDER_UPDATE, mapper.writeValueAsString(orderMessageResponse));
    }
}
