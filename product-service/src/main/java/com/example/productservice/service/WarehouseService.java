package com.example.productservice.service;

import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.orderservice.request.WareHouseCheckingReq;

import java.util.List;
import java.util.Map;

public interface WarehouseService {
    Map<String, List<String>> checkWarehouse (List<WareHouseCheckingReq> request) throws InvalidException, NotFoundException;

    Map<String, List<String>> groupDetails (List<String> productKey);
}
