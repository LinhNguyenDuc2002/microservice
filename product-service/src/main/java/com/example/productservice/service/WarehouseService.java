package com.example.productservice.service;

import com.example.productservice.exception.InvalidException;

import java.util.Map;

public interface WarehouseService {
    Boolean checkWarehouse (Map<String, Integer> productList) throws InvalidException;
}
