package com.example.productservice.service;

import com.example.productservice.dto.statistic.ProductStatisticsDTO;
import com.example.productservice.exception.NotFoundException;

public interface DashboardService {
    ProductStatisticsDTO productStatistics(String id) throws NotFoundException;
}
