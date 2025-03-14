package com.example.userservice.service;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.dto.RoleDto;
import com.example.userservice.dto.request.RoleRequest;

import java.util.List;

public interface RoleService {
    List<RoleDto> getAll();

    RoleDto get(String id) throws I18nException;

    RoleDto update(String id, RoleRequest roleRequest) throws I18nException;

    void assignRole(String roleName, String userId) throws I18nException;

    void unassignRole(String roleName, String userId) throws I18nException;
}
