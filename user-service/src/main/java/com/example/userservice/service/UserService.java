package com.example.userservice.service;

import com.example.userservice.dto.UserAddressDTO;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.request.OTPAuthenticationRequest;
import com.example.userservice.dto.request.UserRegistration;
import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto getLoggedInUser() throws NotFoundException;

    Map<String, String> createTempUser(UserRegistration newUserRegistration) throws ValidationException, JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException;

    void delete(String id) throws NotFoundException;

    List<UserDto> getAll();

    UserDto get(String id) throws NotFoundException;

    UserAddressDTO update(String id, UserRequest userRequest) throws NotFoundException, ValidationException;

    UserDto createUser(OTPAuthenticationRequest request) throws ValidationException, NotFoundException, JsonProcessingException;
}
