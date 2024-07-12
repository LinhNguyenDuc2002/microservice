package com.example.orderservice.service;

import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;

import java.util.Map;

public interface ProductService {
    ProductCheckingResponse checkProductExist(String productId, Integer quantity) throws Exception;

    boolean checkWarehouse(Map<String, Integer> params) throws Exception;
}
