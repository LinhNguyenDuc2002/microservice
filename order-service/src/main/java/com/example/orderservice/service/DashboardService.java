package com.example.orderservice.service;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    Map<String, Double> calculateSales(List<String> productList);
}
