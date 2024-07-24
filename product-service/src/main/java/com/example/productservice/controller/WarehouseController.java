package com.example.productservice.controller;

import com.example.productservice.exception.InvalidException;
import com.example.productservice.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouse")
@Slf4j
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @PatchMapping
    public ResponseEntity<Map<String, List<String>>> checkWarehouse(@RequestBody Map<String, Integer> formData) throws InvalidException {
        return ResponseEntity.ok(warehouseService.checkWarehouse(formData));
    }

    @PostMapping("/detail-group")
    public ResponseEntity<Map<String, List<String>>> groupDetails(@RequestBody List<String> formData) throws InvalidException {
        return ResponseEntity.ok(warehouseService.groupDetails(formData));
    }

    //export inventory file
}
