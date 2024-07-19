package com.example.productservice.service;

import com.example.productservice.dto.statistic.ProductStatisticsDTO;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.response.PageResponse;

import java.util.Date;
import java.util.List;

public interface DashboardService {
    PageResponse<ProductStatisticsDTO> productStatistics(Integer page, Integer size, String shopId, String categoryId) throws Exception;
}
