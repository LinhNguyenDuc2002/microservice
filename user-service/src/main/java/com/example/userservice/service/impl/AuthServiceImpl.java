package com.example.userservice.service.impl;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.config.JwtConfig;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.dto.request.PasswordRequest;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.util.SecurityUtils;
import com.example.userservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final Integer PASSWORD_LENGTH = 6;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void changePwd(PasswordRequest passwordRequest) throws I18nException {
        log.info("Change password");

        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw I18nException.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .message(I18nMessage.ERROR_USER_UNKNOWN)
                    .build();
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.UNAUTHORIZED)
                            .message(I18nMessage.ERROR_USER_UNKNOWN)
                            .build();
                });

        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            log.error("Old password and new password don't match");
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_OLD_PASSWORD_WRONG)
                    .object(passwordRequest)
                    .build();
        }

        if (passwordRequest.getNewPassword().equals(passwordRequest.getOldPassword())) {
            log.error("Old password and new password are the same");
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_PASSWORD_INVALID)
                    .object(passwordRequest)
                    .build();
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        log.info("Changed password of user {} successfully", userId.get());
        userRepository.save(user);
    }

    @Override
    public void resetPwd(String id, String password) throws I18nException {
        log.info("Reset password of user {}", id);

        if (password.length() < PASSWORD_LENGTH || password.isEmpty()) {
            log.error("New password is invalid");
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_PASSWORD_INVALID)
                    .object(password)
                    .build();
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User {} don't exist", id);
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        user.setPassword(passwordEncoder.encode(password));

        log.info("Reset password of user {} successfully", id);
        userRepository.save(user);
    }
}
