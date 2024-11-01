package com.example.userservice.mapper;

import com.example.userservice.dto.PermissionDto;
import com.example.userservice.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper extends AbstractMapper<Permission, PermissionDto> {
    @Override
    public Class<PermissionDto> getDtoClass() {
        return PermissionDto.class;
    }

    @Override
    public Class<Permission> getEntityClass() {
        return Permission.class;
    }
}
