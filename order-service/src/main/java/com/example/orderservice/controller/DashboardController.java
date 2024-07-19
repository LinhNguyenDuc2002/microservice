package com.example.orderservice.controller;

import com.example.orderservice.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @PostMapping("/sales")
    public ResponseEntity<Map<String, Double>> calculateSales(@RequestBody List<String> productList) throws Exception {
        return ResponseEntity.ok(dashboardService.calculateSales(productList));
    }
}
