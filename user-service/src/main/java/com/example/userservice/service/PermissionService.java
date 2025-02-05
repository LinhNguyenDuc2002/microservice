package com.example.userservice.service;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.dto.PermissionDto;
import com.example.userservice.dto.request.PermissionRequest;

import java.util.List;

public interface PermissionService {
    List<PermissionDto> getAll();

    PermissionDto get(String id) throws I18nException;

    List<PermissionDto> getByRoleId(String id) throws I18nException;

    PermissionDto update(String id, PermissionRequest permissionRequest) throws I18nException;
}
