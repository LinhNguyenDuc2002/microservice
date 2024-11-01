package com.example.userservice.service;

import com.example.userservice.dto.RoleDto;
import com.example.userservice.dto.request.RoleRequest;
import com.example.userservice.exception.NotFoundException;

import java.util.List;

public interface RoleService {
    List<RoleDto> getAll();

    RoleDto get(String id) throws NotFoundException;

    RoleDto update(String id, RoleRequest roleRequest) throws NotFoundException;

    void assignRole(String roleName, String userId) throws NotFoundException;

    void unassignRole(String roleName, String userId) throws NotFoundException;
}
