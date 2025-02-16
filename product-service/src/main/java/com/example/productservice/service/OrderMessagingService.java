package com.example.productservice.service;

import com.example.productservice.payload.message.OrderMessageResponse;
import com.example.productservice.payload.orderservice.request.UpdateOrderDetailReq;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderMessagingService {
    void updateOrderDetail(UpdateOrderDetailReq request) throws JsonProcessingException;

    void updateOrderStatus(OrderMessageResponse orderMessageResponse) throws JsonProcessingException;

//    void createCustomer(String customerRequest) throws JsonProcessingException, InvalidationException;
}
