package com.example.userservice.cache.redis;

import com.example.userservice.cache.UserCacheManager;
import com.example.userservice.constant.ExceptionMessage;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.redis.model.UserCache;
import com.example.userservice.redis.repo.UserCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserCacheManagerImpl implements UserCacheManager {
    @Autowired
    private UserCacheRepository userCacheRepository;

    @Override
    public void storeUserCache(UserCache userCache) {
        userCacheRepository.save(userCache);
    }

    @Override
    public UserCache verifyUserCache(String id, String otp, String secret) throws NotFoundException, ValidationException {
        Optional<UserCache> check = userCacheRepository.findById(id);
        if (!check.isPresent()) {
            throw NotFoundException.builder()
                    .message(ExceptionMessage.ERROR_USER_NOT_FOUND)
                    .build();
        }

        UserCache userCache = check.get();
        if (!userCache.getSecretKey().equals(secret) || !userCache.getOtp().equals(otp)) {
            throw ValidationException.builder()
                    .errorObject(otp)
                    .message(ExceptionMessage.ERROR_INVALID_OTP)
                    .build();
        }

        return userCache;
    }

    @Override
    public void clearUserCache(String id) {
        if (StringUtils.hasText(id)) {
            boolean check = userCacheRepository.existsById(id);
            if (check) {
                userCacheRepository.deleteById(id);
            }
        }
        userCacheRepository.deleteAll();
    }
}
