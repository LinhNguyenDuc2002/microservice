package com.example.orderservice.service.impl;

import com.example.orderservice.config.ProductConfiguration;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.webclient.WebClientProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductConfiguration productConfiguration;

    @Autowired
    private WebClientProcessor webClientProcessor;

    @Override
    public ProductCheckingResponse checkProductExist(String productId, Integer quantity) throws Exception {
        String url = productConfiguration.getProductCheckingUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, Object> pathVariable = new HashMap<>();
        pathVariable.put(ProductConfiguration.PATH_UUID, productId);
        uriBuilder.uriVariables(pathVariable);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("quantity", Collections.singletonList(String.valueOf(quantity)));
        Map<String, String> header = new LinkedHashMap<>();

        // send request to product service
        return webClientProcessor.get(
                uriBuilder.toUriString(),
                header,
                params,
                ProductCheckingResponse.class
        );
    }

    @Override
    public boolean checkWarehouse(Map<String, Integer> productList) throws Exception {
        String url = productConfiguration.getCheckWarehouseUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, String> header = new LinkedHashMap<>();

        // send request to product service
        return webClientProcessor.patch(
                uriBuilder.toUriString(),
                header,
                productList,
                Boolean.class
        );
    }
}
