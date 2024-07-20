package com.example.productservice.service.impl;

import com.example.productservice.config.OrderServiceConfiguration;
import com.example.productservice.config.UserServiceConfiguration;
import com.example.productservice.constant.SecurityConstant;
import com.example.productservice.payload.orderservice.response.CheckingDetailResponse;
import com.example.productservice.security.SecurityUtils;
import com.example.productservice.service.UserService;
import com.example.productservice.webclient.WebClientProcessor;
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
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserServiceConfiguration userServiceConfiguration;

    @Autowired
    private WebClientProcessor webClientProcessor;

    @Override
    public void assignRole(String role, String userId) throws Exception {
        String url = userServiceConfiguration.getAssignRoleUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("role", Collections.singletonList(role));
        params.put("user", Collections.singletonList(userId));

        Map<String, String> header = new LinkedHashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, String.format(SecurityConstant.ACCESS_TOKEN_FORMAT, SecurityUtils.getCurrentJWT()));

        // send request to product service
        webClientProcessor.patch(
                uriBuilder.toUriString(),
                header,
                params,
                Void.class
        );
    }

    @Override
    public void unassignRole(String role, String userId) throws Exception {
        String url = userServiceConfiguration.getUnassignRoleUrl();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.put("role", Collections.singletonList(role));
        queryParam.put("user", Collections.singletonList(userId));

        Map<String, String> header = new LinkedHashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, String.format(SecurityConstant.ACCESS_TOKEN_FORMAT, SecurityUtils.getCurrentJWT()));

        // send request to product service
        webClientProcessor.delete(
                uriBuilder.toUriString(),
                header,
                queryParam
        );
    }
}
