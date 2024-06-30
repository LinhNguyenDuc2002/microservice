package com.example.productservice.controller;

import com.example.productservice.dto.statistic.ProductStatisticsDTO;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.service.DashboardService;
import com.example.productservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/product/shop/{id}")
    public ResponseEntity<CommonResponse<ProductStatisticsDTO>> productStatistics(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(dashboardService.productStatistics(id), "");
    }
}
