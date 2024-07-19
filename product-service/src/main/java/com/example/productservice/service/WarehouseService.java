package com.example.productservice.service;

import com.example.productservice.exception.InvalidException;

import java.util.List;
import java.util.Map;

public interface WarehouseService {
    Map<String, List<String>> checkWarehouse (Map<String, Integer> productList) throws InvalidException;
}
