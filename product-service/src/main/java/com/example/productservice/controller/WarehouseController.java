package com.example.productservice.controller;

import com.example.productservice.dto.WareHouseCheckingDto;
import com.example.productservice.payload.orderservice.request.WareHouseCheckingReq;
import com.example.productservice.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
@Slf4j
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @PatchMapping
    public ResponseEntity<WareHouseCheckingDto> checkWarehouse(@RequestBody List<WareHouseCheckingReq> request) {
        return ResponseEntity.ok(warehouseService.checkWarehouse(request));
    }

    //export inventory file
}
