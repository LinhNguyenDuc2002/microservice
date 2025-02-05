package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.PageDto;
import com.example.orderservice.dto.request.OrderProductRequest;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.servicefoundation.exception.I18nException;

import java.util.Date;
import java.util.List;

public interface OrderService {
    OrderDto create(OrderRequest orderRequest) throws Exception;

    OrderDto create(OrderProductRequest orderProductRequest) throws Exception;

    OrderDto update(String orderId, String receiverId) throws I18nException;

    PageDto<OrderDto> getAll(Integer page, Integer size, Date startAt, Date endAt, String status, List<String> sortColumns) throws Exception;

    OrderDto get(String id) throws I18nException;

    PageDto<OrderDto> getByCustomerId(Integer page, Integer size, String status, String id, List<String> sortColumns);

    OrderDto changeStatus(String id, String status) throws I18nException;

    void delete(String id) throws I18nException;
}
