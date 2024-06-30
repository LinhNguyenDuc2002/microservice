package com.example.userservice.service.impl;

import com.example.userservice.config.JwtConfig;
import com.example.userservice.constant.ExceptionMessage;
import com.example.userservice.dto.request.PasswordRequest;
import com.example.userservice.entity.User;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.UnauthorizedException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.AuthService;
import com.example.userservice.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void changePwd(PasswordRequest passwordRequest) throws NotFoundException, ValidationException {
        log.info("Change password");

        Optional<String> userId = SecurityUtil.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw new UnauthorizedException(ExceptionMessage.ERROR_USER_UNKNOWN);
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return new UnauthorizedException(ExceptionMessage.ERROR_USER_UNKNOWN);
                });

        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            log.error("Old password and new password don't match");
            throw new ValidationException(passwordRequest, ExceptionMessage.ERROR_OLD_PASSWORD_INVALID);
        }

        if (passwordRequest.getNewPassword().equals(passwordRequest.getOldPassword())) {
            log.error("Old password and new password are the same");
            throw new ValidationException(passwordRequest, ExceptionMessage.ERROR_INPUT_PASSWORD_INVALID);
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        log.info("Changed password of user {} successfully", userId.get());
        userRepository.save(user);
    }

    @Override
    public void resetPwd(String id, String password) throws NotFoundException, ValidationException {
        log.info("Reset password of user {}", id);

        if (password.length() < 6 || password.isEmpty()) {
            log.error("New password is invalid");
            throw new ValidationException(password, ExceptionMessage.ERROR_INPUT_PASSWORD_INVALID);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ExceptionMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        user.setPassword(passwordEncoder.encode(password));

        log.info("Reset password of user {} successfully", id);
        userRepository.save(user);
    }
}
