package com.example.userservice.controller;

import com.example.servicefoundation.base.response.Response;
import com.example.servicefoundation.i18n.I18nService;
import com.example.servicefoundation.util.ResponseUtil;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.constant.SecurityConstant;
import com.example.userservice.dto.request.PasswordRequest;
import com.example.userservice.exception.InvalidationException;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private I18nService i18nService;

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
    @PutMapping("/password")
    public ResponseEntity<Response<Void>> changePassword(@Valid @RequestBody PasswordRequest pwd) throws InvalidationException, NotFoundException {
        authService.changePwd(pwd);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_CHANGE_PASSWORD, LocaleContextHolder.getLocale())
        );
    }

    @PostMapping("/password")
    public ResponseEntity<Response<Void>> forgetPassword(
            @RequestParam(name = "password") String password,
            @PathVariable String id) throws InvalidationException, NotFoundException {
        authService.resetPwd(id, password);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_RESET_PASSWORD, LocaleContextHolder.getLocale())
        );
    }

    @PatchMapping("/password/{id}")
    @Secured({SecurityConstant.ADMIN, SecurityConstant.EMPLOYEE})
    public ResponseEntity<Response<Void>> resetPassword(
            @RequestParam(name = "password") String password,
            @PathVariable String id) throws InvalidationException, NotFoundException {
        authService.resetPwd(id, password);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_RESET_PASSWORD, LocaleContextHolder.getLocale())
        );
    }
}
