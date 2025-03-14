package com.example.productservice.service;

import com.example.productservice.dto.ProductDetailCheckingDto;
import com.example.productservice.dto.ProductDetailsCheckingDto;
import com.example.productservice.payload.orderservice.request.ProductCheckingRequest;

import java.util.List;

public interface WarehouseService {
    ProductDetailsCheckingDto checkListProduct(List<ProductCheckingRequest> request);

    ProductDetailCheckingDto checkProduct(ProductCheckingRequest request);
}
