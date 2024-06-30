package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.ValidationException;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto getLoggedInUser() throws NotFoundException;

    Map<String, String> createTempUser(UserRequest newUserRequest) throws ValidationException;

    void delete(String id) throws NotFoundException;

    List<UserDto> getAll();

    UserDto get(String id) throws NotFoundException;

    UserDto update(String id, UserRequest userRequest) throws NotFoundException, ValidationException;

    UserDto createUser(String id, String otp, String secret) throws ValidationException, NotFoundException;
}
