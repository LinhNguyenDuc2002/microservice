package com.example.orderservice.service;

import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductCheckingResponse checkProductExist(String productId, Integer quantity) throws Exception;

    Map<String, List<String>> checkWarehouse(Map<String, Integer> params) throws Exception;

    Boolean checkShopExist(String shopId) throws Exception;

    Map<String, List<String>> groupDetails(List<String> body) throws Exception;
}
