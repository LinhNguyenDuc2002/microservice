package com.example.userservice.service.impl;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.constant.RoleType;
import com.example.userservice.dto.RoleDto;
import com.example.userservice.dto.request.RoleRequest;
import com.example.userservice.entity.Permission;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.RoleMapper;
import com.example.userservice.repository.PermissionRepository;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleDto> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toDtoList(roles);
    }

    @Override
    public RoleDto get(String id) throws I18nException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .message("")
                            .build();
                });
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto update(String id, RoleRequest roleRequest) throws I18nException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_ROLE_NOT_FOUND)
                            .build();
                });
        List<Permission> permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(permissions);
        role.setDescription(roleRequest.getDescription());
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    @Override
    public void assignRole(String roleName, String userId) throws I18nException {
        Role role = roleRepository.findByName(RoleType.valueOf(roleName));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });
        if (user.getRoles().contains(role)) {
            throw I18nException.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .message(I18nMessage.ERROR_USER_ASSIGN_ROLE)
                    .build();
        }

        user.getRoles().add(role);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void unassignRole(String roleName, String userId) throws I18nException {
        Role role = roleRepository.findByName(RoleType.valueOf(roleName));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            role.getUsers().remove(user);
            userRepository.save(user);
            roleRepository.save(role);
        }
    }
}
