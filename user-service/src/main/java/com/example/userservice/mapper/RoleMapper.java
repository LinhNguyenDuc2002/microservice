package com.example.userservice.mapper;

import com.example.userservice.dto.RoleDto;
import com.example.userservice.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper extends AbstractMapper<Role, RoleDto> {
    @Override
    public Class<RoleDto> getDtoClass() {
        return RoleDto.class;
    }

    @Override
    public Class<Role> getEntityClass() {
        return Role.class;
    }
}
