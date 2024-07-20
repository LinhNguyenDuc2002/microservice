package com.example.userservice.service;

import com.example.userservice.exception.NotFoundException;

public interface RoleService {
    void assignRole(String roleName, String userId) throws NotFoundException;

    void unassignRole(String roleName, String userId) throws NotFoundException;
}
