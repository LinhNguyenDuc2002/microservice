package com.example.productservice.controller;

import com.example.productservice.dto.ProductDetailCheckingDto;
import com.example.productservice.dto.ProductDetailsCheckingDto;
import com.example.productservice.payload.orderservice.request.ProductCheckingRequest;
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
    public ResponseEntity<ProductDetailsCheckingDto> checkListProduct(@RequestBody List<ProductCheckingRequest> request) {
        return ResponseEntity.ok(warehouseService.checkListProduct(request));
    }

    @PatchMapping("/product")
    public ResponseEntity<ProductDetailCheckingDto> checkProduct(@RequestBody ProductCheckingRequest request) {
        return ResponseEntity.ok(warehouseService.checkProduct(request));
    }

    //export inventory file
}
