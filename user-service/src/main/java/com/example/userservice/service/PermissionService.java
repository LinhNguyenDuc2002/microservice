package com.example.userservice.service;

import com.example.userservice.dto.PermissionDto;
import com.example.userservice.dto.request.PermissionRequest;
import com.example.userservice.exception.InvalidationException;
import com.example.userservice.exception.NotFoundException;

import java.util.List;

public interface PermissionService {
    List<PermissionDto> getAll();

    PermissionDto get(String id) throws NotFoundException;

    List<PermissionDto> getByRoleId(String id) throws NotFoundException;

    PermissionDto update(String id, PermissionRequest permissionRequest) throws NotFoundException, InvalidationException;
}
