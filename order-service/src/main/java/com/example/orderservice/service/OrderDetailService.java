package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDetailDto;
import com.example.orderservice.dto.PageDto;
import com.example.servicefoundation.exception.I18nException;

public interface OrderDetailService {
    OrderDetailDto create(String productDetailId, Integer quantity) throws Exception;

    OrderDetailDto update(String id, Integer quantity) throws Exception;

    PageDto<OrderDetailDto> getAll(Integer page, Integer size, String customerId, String status) throws Exception;

    OrderDetailDto get(String id) throws I18nException;

    void delete(String id) throws I18nException;
}
