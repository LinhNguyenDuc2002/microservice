package com.example.userservice.controller;

import com.example.servicefoundation.base.response.Response;
import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.i18n.I18nService;
import com.example.servicefoundation.util.ResponseUtil;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.dto.BasicUserInfoDto;
import com.example.userservice.dto.UserAddressDTO;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.request.OTPAuthenticationRequest;
import com.example.userservice.dto.request.UpdateInfo;
import com.example.userservice.dto.request.UserRegistration;
import com.example.userservice.dto.request.UserRegistrationHasRole;
import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private I18nService i18nService;

    @GetMapping("/me")
    public ResponseEntity<Response<UserDto>> getLoggedInUser() throws I18nException {
        return ResponseUtil.wrapResponse(
                userService.getLoggedInUser(),
                i18nService.getMessage(I18nMessage.INFO_GET_USER, LocaleContextHolder.getLocale())
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<Response<UserDto>> verifyOTPToCreateUser(@RequestBody OTPAuthenticationRequest request) throws JsonProcessingException, I18nException {
        return ResponseUtil.wrapResponse(
                userService.createUser(request),
                i18nService.getMessage(I18nMessage.INFO_CREATE_ACCOUNT, LocaleContextHolder.getLocale())
        );
    }

    @PostMapping("/customer")
    public ResponseEntity<Response<Object>> createTempUser(@Valid @RequestBody UserRegistration userRegistration) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException, I18nException {
        return ResponseUtil.wrapResponse(
                userService.createTempUser(userRegistration),
                i18nService.getMessage(I18nMessage.INFO_WAIT_OTP, LocaleContextHolder.getLocale())
        );
    }

    @PostMapping
    public ResponseEntity<Response<Object>> createUser(@Valid @RequestBody UserRegistrationHasRole userRegistration) {
        return ResponseUtil.wrapResponse(
                userService.createUser(userRegistration),
                i18nService.getMessage(I18nMessage.INFO_WAIT_OTP, LocaleContextHolder.getLocale())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<UserDto>> delete(@PathVariable String id) throws I18nException {
        userService.delete(id);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_DELETE_ACCOUNT, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping
//    @Secured({SecurityConstant.ADMIN, SecurityConstant.EMPLOYEE})
    public ResponseEntity<Response<List<UserDto>>> getAll() {
        return ResponseUtil.wrapResponse(
                userService.getAll(),
                i18nService.getMessage(I18nMessage.INFO_GET_ALL_USER, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserDto>> get(@PathVariable String id) throws I18nException {
        return ResponseUtil.wrapResponse(
                userService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_USER, LocaleContextHolder.getLocale())
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response<UserAddressDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateInfo updateInfo) throws I18nException {
        return ResponseUtil.wrapResponse(
                userService.update(id, updateInfo),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_USER, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping
    public ResponseEntity<Response<UserDto>> update(@Valid @ModelAttribute UserRequest userRequest) throws I18nException, IOException {
        return ResponseUtil.wrapResponse(
                userService.update(userRequest),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_USER, LocaleContextHolder.getLocale())
        );
    }

    @PostMapping("/ìnfo")
    public ResponseEntity<List<BasicUserInfoDto>> getUserInfo(@RequestBody List<String> ids) {
        return ResponseEntity.ok(userService.getUserInfo(ids));
    }
}
