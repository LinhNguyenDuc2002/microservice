package com.example.userservice.controller;

import com.example.userservice.constant.ResponseMessage;
import com.example.userservice.constant.SecurityConstant;
import com.example.userservice.dto.UserAddressDTO;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.request.OTPAuthenticationRequest;
import com.example.userservice.dto.request.UserRegistration;
import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.dto.response.CommonResponse;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.service.UserService;
import com.example.userservice.util.HandleBindingResult;
import com.example.userservice.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<UserDto>> getLoggedInUser() throws NotFoundException {
        return ResponseUtil.wrapResponse(userService.getLoggedInUser(), ResponseMessage.GET_USER_SUCCESS.getMessage());
    }

    @PostMapping("/verify")
    public ResponseEntity<CommonResponse<UserDto>> verifyOTPToCreateUser(@RequestBody OTPAuthenticationRequest request) throws ValidationException, NotFoundException, JsonProcessingException {
        return ResponseUtil.wrapResponse(userService.createUser(request), ResponseMessage.CREATE_USER_SUCCESS.getMessage());
    }

    @PostMapping
    public ResponseEntity<CommonResponse<Object>> createTempUser(
            @Valid @RequestBody UserRegistration newUserRegistration,
            BindingResult bindingResult) throws ValidationException, JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        HandleBindingResult.handle(bindingResult, newUserRegistration);
        return ResponseUtil.wrapResponse(userService.createTempUser(newUserRegistration), ResponseMessage.WAIT_ENTER_OTP.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<UserDto>> delete(@PathVariable String id) throws NotFoundException {
        userService.delete(id);
        return ResponseUtil.wrapResponse(null, ResponseMessage.DELETE_USER_SUCCESS.getMessage());
    }

    @GetMapping
    @Secured({SecurityConstant.ADMIN, SecurityConstant.EMPLOYEE})
    public ResponseEntity<CommonResponse<List<UserDto>>> getAll() {
        return ResponseUtil.wrapResponse(userService.getAll(), ResponseMessage.GET_ALL_USER_SUCCESS.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<UserDto>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(userService.get(id), ResponseMessage.GET_USER_SUCCESS.getMessage());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<UserAddressDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody UserRequest userRequest,
            BindingResult bindingResult) throws NotFoundException, ValidationException {
        HandleBindingResult.handle(bindingResult, userRequest);
        return ResponseUtil.wrapResponse(userService.update(id, userRequest), ResponseMessage.UPDATE_USER_SUCCESS.getMessage());
    }
}
