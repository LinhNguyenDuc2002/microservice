package com.example.userservice.service;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.dto.request.PasswordRequest;

public interface AuthService {
    void changePwd(PasswordRequest passwordRequest) throws I18nException;

    void resetPwd(String id, String password) throws I18nException;
}
