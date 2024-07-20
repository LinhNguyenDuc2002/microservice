package com.example.userservice.service.impl;

import com.example.userservice.constant.ExceptionMessage;
import com.example.userservice.constant.RoleType;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.UnauthorizedException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void assignRole(String roleName, String userId) throws NotFoundException {
        Role role = roleRepository.findByRoleName(RoleType.valueOf(roleName));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_USER_NOT_FOUND);
                });
        if(user.getRoles().contains(role)) {
            throw new UnauthorizedException(ExceptionMessage.ERROR_USER_ASSIGN_ROLE_ALREADY);
        }

        user.getRoles().add(role);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void unassignRole(String roleName, String userId) throws NotFoundException {
        Role role = roleRepository.findByRoleName(RoleType.valueOf(roleName));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_USER_NOT_FOUND);
                });

        if(user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            role.getUsers().remove(user);
            userRepository.save(user);
            roleRepository.save(role);
        }
    }
}
