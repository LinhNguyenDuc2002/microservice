package com.example.productservice.service;

import com.example.productservice.payload.orderservice.response.CheckingDetailResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {
    CheckingDetailResponse checkDetailExist(String id) throws Exception;

    Map<String, Double> calculateSales(List<String> productList) throws Exception;
}
