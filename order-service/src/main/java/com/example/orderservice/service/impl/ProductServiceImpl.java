package com.example.orderservice.service.impl;

import com.example.orderservice.config.ProductConfiguration;
import com.example.orderservice.constant.SecurityConstant;
import com.example.orderservice.payload.productservice.request.WareHouseCheckingReq;
import com.example.orderservice.payload.productservice.response.ProductCheckingResponse;
import com.example.orderservice.payload.productservice.response.WareHouseCheckingResponse;
import com.example.orderservice.security.SecurityUtil;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.webclient.WebClientProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductConfiguration productConfiguration;

    @Autowired
    private WebClientProcessor webClientProcessor;

    @Override
    public ProductCheckingResponse checkProductExist(String productDetailId, Integer quantity) throws Exception {
        String url = productConfiguration.getProductCheckingUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, Object> pathVariable = new HashMap<>();
        pathVariable.put(ProductConfiguration.PATH_UUID, productDetailId);
        uriBuilder.uriVariables(pathVariable);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put(ProductConfiguration.QUANTITY, Collections.singletonList(String.valueOf(quantity)));
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
    public WareHouseCheckingResponse checkWarehouse(List<WareHouseCheckingReq> wareHouseCheckingReqs) throws Exception {
        String url = productConfiguration.getCheckWarehouseUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, String> header = new LinkedHashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, String.format(SecurityConstant.ACCESS_TOKEN_FORMAT, SecurityUtil.getCurrentJWT()));

        // send request to product service
        return webClientProcessor.patch(
                uriBuilder.toUriString(),
                header,
                wareHouseCheckingReqs,
                WareHouseCheckingResponse.class
        );
    }

    @Override
    public Boolean checkShopExist(String shopId) throws Exception {
        String url = productConfiguration.getCheckShopUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, Object> pathVariable = new HashMap<>();
        pathVariable.put(ProductConfiguration.PATH_UUID, shopId);
        uriBuilder.uriVariables(pathVariable);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        Map<String, String> header = new LinkedHashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, String.format(SecurityConstant.ACCESS_TOKEN_FORMAT, SecurityUtil.getCurrentJWT()));

        return webClientProcessor.get(
                uriBuilder.toUriString(),
                header,
                params,
                Boolean.class
        );
    }

    @Override
    public Map<String, List<String>> groupDetails(List<String> body) throws Exception {
        String url = productConfiguration.getGroupDetailsUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, String> header = new LinkedHashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, String.format(SecurityConstant.ACCESS_TOKEN_FORMAT, SecurityUtil.getCurrentJWT()));

        // send request to product service
        return webClientProcessor.post(
                uriBuilder.toUriString(),
                header,
                body,
                Map.class
        );
    }
}