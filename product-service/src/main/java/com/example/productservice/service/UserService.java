package com.example.productservice.service;

import com.example.productservice.payload.userservice.response.CustomerInfoResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    void assignRole(String role, String userId) throws Exception;

    void unassignRole(String role, String userId) throws Exception;

    Map<String, CustomerInfoResponse> getUserInfo(List<String> ids) throws Exception;
}
