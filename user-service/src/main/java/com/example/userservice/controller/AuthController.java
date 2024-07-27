package com.example.userservice.controller;

import com.example.userservice.constant.ResponseMessage;
import com.example.userservice.constant.SecurityConstant;
import com.example.userservice.dto.request.PasswordRequest;
import com.example.userservice.dto.response.CommonResponse;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.service.AuthService;
import com.example.userservice.util.HandleBindingResult;
import com.example.userservice.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    /*
     * /oauth2/token: return token
     */

    /**
     * https://www.facebook.com/v15.0/dialog/oauth?client_id=1530299457838583&redirect_uri=http://localhost:8080/api/auth/user&scope=public_profile,email&response_type=code
     * https://www.facebook.com/v15.0/dialog/oauth?client_id=1530299457838583&redirect_uri=http://localhost:8080/api/login/oauth2/code/facebook&scope=public_profile,email&response_type=code
     *
     * @param
     * @return
     */

    @PutMapping("/change-pwd")
    public ResponseEntity<CommonResponse<Void>> changePassword(
            @Valid @RequestBody PasswordRequest pwd,
            BindingResult bindingResult) throws ValidationException, NotFoundException {
        HandleBindingResult.handle(bindingResult, pwd);
        authService.changePwd(pwd);
        return ResponseUtil.wrapResponse(null, ResponseMessage.CHANGE_PASSWORD_SUCCESS.getMessage());
    }

    @PutMapping("/{id}/reset-pwd")
    @Secured({SecurityConstant.ADMIN, SecurityConstant.EMPLOYEE})
    public ResponseEntity<CommonResponse<Void>> resetPassword(
            @RequestParam(name = "password") String password,
            @PathVariable String id) throws ValidationException, NotFoundException {
        authService.resetPwd(id, password);
        return ResponseUtil.wrapResponse(null, ResponseMessage.RESET_PASSWORD_SUCCESS.getMessage());
    }
}
