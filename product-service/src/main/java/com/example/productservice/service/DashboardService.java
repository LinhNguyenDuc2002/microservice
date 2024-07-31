package com.example.productservice.service;

import com.example.productservice.dto.statistic.ProductStatisticsDTO;
import com.example.productservice.dto.PageDTO;

import java.util.List;

public interface DashboardService {
    PageDTO<ProductStatisticsDTO> productStatistics(Integer page, Integer size, String shopId, String categoryId, List<String> sortColumns) throws Exception;
}
