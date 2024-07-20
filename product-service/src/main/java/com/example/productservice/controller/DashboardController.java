package com.example.productservice.controller;

import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.dto.statistic.ProductStatisticsDTO;
import com.example.productservice.payload.response.PageResponse;
import com.example.productservice.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/products")
    public ResponseEntity<PageResponse<ProductStatisticsDTO>> productStatistics(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = "20") Integer size,
            @RequestParam(name = "shop", required = false) String shopId,
            @RequestParam(name = "category", required = false) String categoryId) throws Exception {
        return ResponseEntity.ok(dashboardService.productStatistics(page, size, shopId, categoryId));
    }
}
