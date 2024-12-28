package com.example.orderservice.service;

import com.example.orderservice.payload.productservice.request.WareHouseCheckingReq;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.payload.productservice.response.WareHouseCheckingResponse;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductCheckingResponse checkProductExist(String productDetailId, Integer quantity) throws Exception;

    WareHouseCheckingResponse checkWarehouse(List<WareHouseCheckingReq> wareHouseCheckingReqs) throws Exception;

    Boolean checkShopExist(String shopId) throws Exception;

    Map<String, List<String>> groupDetails(List<String> body) throws Exception;
}