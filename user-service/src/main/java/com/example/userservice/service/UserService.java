package com.example.userservice.service;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.dto.BasicUserInfoDto;
import com.example.userservice.dto.UserAddressDTO;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.request.OTPAuthenticationRequest;
import com.example.userservice.dto.request.UpdateInfo;
import com.example.userservice.dto.request.UserRegistration;
import com.example.userservice.dto.request.UserRegistrationHasRole;
import com.example.userservice.dto.request.UserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto getLoggedInUser() throws I18nException;

    Map<String, String> createTempUser(UserRegistration userRegistration) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException, I18nException;

    void delete(String id) throws I18nException;

    List<UserDto> getAll();

    UserDto get(String id) throws I18nException;

    UserAddressDTO update(String id, UserRequest userRequest) throws I18nException;

    UserAddressDTO update(String id, UpdateInfo updateInfo) throws I18nException;

    UserDto createUser(OTPAuthenticationRequest request) throws JsonProcessingException, I18nException;

    UserDto createUser(UserRegistrationHasRole userRegistration);

    List<BasicUserInfoDto> getUserInfo(List<String> ids);
}
