package com.example.userservice.cache.redis;

import com.example.userservice.cache.UserCacheManager;
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
    public Optional<UserCache> getUserCache(String secret) {
        return userCacheRepository.findById(secret);
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
