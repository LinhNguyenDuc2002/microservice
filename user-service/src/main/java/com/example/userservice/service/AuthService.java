package com.example.userservice.service;

import com.example.userservice.dto.request.PasswordRequest;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.InvalidationException;

public interface AuthService {
    void changePwd(PasswordRequest passwordRequest) throws NotFoundException, InvalidationException;

    void resetPwd(String id, String password) throws NotFoundException, InvalidationException;
}
