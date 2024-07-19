package com.example.orderservice.service.impl;

import com.example.orderservice.constant.BillStatus;
import com.example.orderservice.entity.Detail;
import com.example.orderservice.repository.BillRepository;
import com.example.orderservice.repository.DetailRepository;
import com.example.orderservice.repository.predicate.DetailPredicate;
import com.example.orderservice.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private DetailRepository detailRepository;

    @Override
    public Map<String, Double> calculateSales(List<String> productList) {
        DetailPredicate detailPredicate = new DetailPredicate()
                .withProductIds(productList)
                .withBillStatus(BillStatus.PAID);
        List<Detail> details = detailRepository.findAll(detailPredicate.getCriteria());

        Map<String, Double> sales = new LinkedHashMap<>();
        for(Detail detail : details) {
            String id = detail.getProduct();
            if(!sales.containsKey(id)) {
                sales.put(id, (double) 0);
            }

            sales.put(id, sales.get(id) + detail.getQuantity()*detail.getUnitPrice());
        }

        return sales;
    }
}
