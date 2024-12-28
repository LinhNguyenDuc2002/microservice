package com.example.productservice.service;

import com.example.productservice.dto.WareHouseCheckingDto;
import com.example.productservice.payload.orderservice.request.WareHouseCheckingReq;

import java.util.List;

public interface WarehouseService {
    WareHouseCheckingDto checkWarehouse(List<WareHouseCheckingReq> request);
}
