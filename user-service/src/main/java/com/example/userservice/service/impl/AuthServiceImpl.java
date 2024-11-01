package com.example.userservice.service.impl;

import com.example.userservice.config.JwtConfig;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.dto.request.PasswordRequest;
import com.example.userservice.entity.User;
import com.example.userservice.exception.InvalidationException;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.UnauthorizedException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.util.SecurityUtils;
import com.example.userservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void changePwd(PasswordRequest passwordRequest) throws NotFoundException, InvalidationException {
        log.info("Change password");

        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw new UnauthorizedException(I18nMessage.ERROR_USER_UNKNOWN);
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return new UnauthorizedException(I18nMessage.ERROR_USER_UNKNOWN);
                });

        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            log.error("Old password and new password don't match");
            throw new InvalidationException(passwordRequest, I18nMessage.ERROR_OLD_PASSWORD_WRONG);
        }

        if (passwordRequest.getNewPassword().equals(passwordRequest.getOldPassword())) {
            log.error("Old password and new password are the same");
            throw new InvalidationException(passwordRequest, I18nMessage.ERROR_PASSWORD_INVALID);
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        log.info("Changed password of user {} successfully", userId.get());
        userRepository.save(user);
    }

    @Override
    public void resetPwd(String id, String password) throws NotFoundException, InvalidationException {
        log.info("Reset password of user {}", id);

        if (password.length() < PASSWORD_LENGTH || password.isEmpty()) {
            log.error("New password is invalid");
            throw new InvalidationException(password, I18nMessage.ERROR_PASSWORD_INVALID);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User {} don't exist", id);
                    return NotFoundException.builder()
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        user.setPassword(passwordEncoder.encode(password));

        log.info("Reset password of user {} successfully", id);
        userRepository.save(user);
    }
}
