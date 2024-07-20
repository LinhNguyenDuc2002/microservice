package com.example.productservice.service;

public interface UserService {
    void assignRole(String role, String userId) throws Exception;

    void unassignRole(String role, String userId) throws Exception;
}
