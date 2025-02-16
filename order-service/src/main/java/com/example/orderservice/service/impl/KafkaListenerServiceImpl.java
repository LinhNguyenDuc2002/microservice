package com.example.orderservice.service.impl;

import com.example.orderservice.constant.KafkaTopic;
import com.example.orderservice.constant.MessageStatus;
import com.example.orderservice.constant.OrderDetailStatus;
import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.entity.PurchaseOrder;
import com.example.orderservice.payload.message.OrderMessageRequest;
import com.example.orderservice.payload.productservice.request.UpdateOrderDetailReq;
import com.example.orderservice.repository.OrderDetailRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.predicate.OrderDetailPredicate;
import com.example.orderservice.service.KafkaListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class KafkaListenerServiceImpl implements KafkaListenerService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

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

    @KafkaListener(topics = KafkaTopic.ORDER_UPDATE)
    @Override
    public void updateOrder(String request) throws JsonProcessingException {
        OrderMessageRequest orderMessageRequest = mapper.readValue(request, OrderMessageRequest.class);
        if(orderMessageRequest.getStatus().equals(MessageStatus.INVENTORY_DEDUCT_SUCCESS)) {
            Optional<PurchaseOrder> check = orderRepository.findById(orderMessageRequest.getId());
            if(check.isPresent()) {
                PurchaseOrder purchaseOrder = check.get();
                purchaseOrder.setStatus(OrderStatus.APPROVED);
                orderRepository.save(purchaseOrder);
            }
        }
    }
}
