package com.example.userservice.service.impl;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.dto.PermissionDto;
import com.example.userservice.dto.request.PermissionRequest;
import com.example.userservice.entity.Permission;
import com.example.userservice.entity.Role;
import com.example.userservice.mapper.PermissionMapper;
import com.example.userservice.repository.PermissionRepository;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<PermissionDto> getAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.toDtoList(permissions);
    }

    @Override
    public PermissionDto get(String id) throws I18nException {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_PERMISSION_NOT_FOUND)
                            .build();
                });

        return permissionMapper.toDto(permission);
    }

    @Override
    public List<PermissionDto> getByRoleId(String id) throws I18nException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_ROLE_NOT_FOUND)
                            .build();
                });

        return permissionMapper.toDtoList(role.getPermissions().stream().toList());
    }

    @Override
    public PermissionDto update(String id, PermissionRequest permissionRequest) throws I18nException {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_PERMISSION_NOT_FOUND)
                            .build();
                });
        if (!permission.getCode().equals(permissionRequest.getCode())) {
            boolean check = permissionRepository.existsByCode(permissionRequest.getCode());
            if (check) {
                throw I18nException.builder()
                        .code(HttpStatus.NOT_FOUND)
                        .message(I18nMessage.ERROR_PERMISSION_CODE_EXISTED)
                        .build();
            }

            permission.setCode(permissionRequest.getCode());
        }
        permission.setName(permissionRequest.getName());
        permission.setDescription(permissionRequest.getDescription());
        permissionRepository.save(permission);

        return permissionMapper.toDto(permission);
    }
}
