package com.example.userservice.service;

import com.example.userservice.dto.request.PasswordRequest;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.ValidationException;

public interface AuthService {
    void changePwd(PasswordRequest passwordRequest) throws NotFoundException, ValidationException;

    void resetPwd(String id, String password) throws NotFoundException, ValidationException;
}
