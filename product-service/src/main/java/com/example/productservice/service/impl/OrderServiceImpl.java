package com.example.productservice.service.impl;

import com.example.productservice.config.OrderConfiguration;
import com.example.productservice.payload.orderservice.response.CheckingDetailResponse;
import com.example.productservice.service.OrderService;
import com.example.productservice.webclient.WebClientProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderConfiguration orderConfiguration;

    @Autowired
    private WebClientProcessor webClientProcessor;
    @Override
    public CheckingDetailResponse checkDetailExist(String id) throws Exception {
        String url = orderConfiguration.getCheckDetailUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, Object> pathVariable = new HashMap<>();
        pathVariable.put(OrderConfiguration.PATH_UUID, id);
        uriBuilder.uriVariables(pathVariable);

        Map<String, String> header = new LinkedHashMap<>();

        // send request to product service
        return webClientProcessor.get(
                uriBuilder.toUriString(),
                header,
                null,
                CheckingDetailResponse.class
        );
    }

    @Override
    public Map<String, Double> calculateSales(List<String> productList) throws Exception {
        String url = orderConfiguration.getCalculateSalesUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, String> header = new LinkedHashMap<>();

        // send request to product service
        return webClientProcessor.post(
                uriBuilder.toUriString(),
                header,
                productList,
                Map.class
        );
    }
}
