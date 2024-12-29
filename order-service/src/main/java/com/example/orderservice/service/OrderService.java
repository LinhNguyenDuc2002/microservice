package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.PageDto;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.exception.InvalidationException;
import com.example.orderservice.exception.NotFoundException;

import java.util.Date;
import java.util.List;

public interface OrderService {
    OrderDto create(OrderRequest orderRequest) throws Exception;

    OrderDto update(String orderId, String receiverId) throws NotFoundException, InvalidationException;

    PageDto<OrderDto> getAll(Integer page, Integer size, Date startAt, Date endAt, String status, List<String> sortColumns) throws Exception;

    OrderDto get(String id) throws NotFoundException;

    PageDto<OrderDto> getByCustomerId(Integer page, Integer size, String status, String id, List<String> sortColumns) throws NotFoundException;

    OrderDto changeStatus(String id, String status) throws NotFoundException, InvalidationException;

    void delete(String id) throws NotFoundException, InvalidationException;
}
