package com.example.productservice.service;

import com.example.productservice.payload.orderservice.response.CheckingDetailResponse;

public interface OrderService {
    CheckingDetailResponse checkDetailExist(String id) throws Exception;
}
