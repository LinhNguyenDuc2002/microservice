package com.example.orderservice.service;

import com.example.orderservice.payload.productservice.request.ProductCheckingReq;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.payload.productservice.response.ProductDetailsCheckingRes;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductCheckingResponse checkProductExist(String productDetailId, Integer quantity) throws Exception;

    ProductDetailsCheckingRes checkProductDetails(List<ProductCheckingReq> productCheckingReqs) throws Exception;

    Boolean checkShopExist(String shopId) throws Exception;

    Map<String, List<String>> groupDetails(List<String> body) throws Exception;

    ProductCheckingResponse checkProduct(ProductCheckingReq productCheckingReq) throws Exception;
}