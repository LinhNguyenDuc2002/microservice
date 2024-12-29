package com.example.orderservice.service.impl;

import com.example.orderservice.constant.KafkaTopic;
import com.example.orderservice.constant.OrderDetailStatus;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.payload.productservice.request.UpdateOrderDetailReq;
import com.example.orderservice.repository.OrderDetailRepository;
import com.example.orderservice.repository.predicate.OrderDetailPredicate;
import com.example.orderservice.service.KafkaListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaListenerServiceImpl implements KafkaListenerService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ObjectMapper mapper;

    @KafkaListener(topics = KafkaTopic.UPDATE_UNIT_PRICE_DETAIL)
    @Override
    public void updateOrderDetail(String products) throws JsonProcessingException {
        UpdateOrderDetailReq request = mapper.readValue(products, UpdateOrderDetailReq.class);

        OrderDetailPredicate orderDetailPredicate = new OrderDetailPredicate()
                .withProductDetailId(request.getProductDetailId())
                .withoutStatus(OrderDetailStatus.PURCHASED);
        List<OrderDetail> orderDetails = orderDetailRepository.findAll(orderDetailPredicate.getCriteria());

        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setUnitPrice(request.getPrice());
            if(orderDetail.getQuantity() > request.getQuantity()) {
                orderDetail.setStatus(OrderDetailStatus.OUT_OF_STOCK);
            }
        }

        orderDetailRepository.saveAll(orderDetails);
    }
}
