package com.example.productservice.controller;

import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.statistic.ProductStatisticsDTO;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.service.DashboardService;
import com.example.productservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/products")
    public ResponseEntity<CommonResponse<PageDTO<ProductStatisticsDTO>>> productStatistics(
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "shop", required = false) String shopId,
            @RequestParam(name = "category", required = false) String categoryId,
            @RequestParam(name = "sort-columns", required = false) List<String> sortColumns) throws Exception {
        return ResponseUtil.wrapResponse(dashboardService.productStatistics(page, size, shopId, categoryId, sortColumns));
    }
}
