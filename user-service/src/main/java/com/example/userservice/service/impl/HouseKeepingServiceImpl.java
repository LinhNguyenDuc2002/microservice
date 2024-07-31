package com.example.userservice.service.impl;

import com.example.userservice.cache.UserCacheManager;
import com.example.userservice.service.HouseKeepingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HouseKeepingServiceImpl implements HouseKeepingService {
    @Autowired
    private UserCacheManager userCacheManager;

    @Override
    public void cleanUserCache() {
        userCacheManager.clearUserCache(null);
    }
}
