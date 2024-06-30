package com.example.userservice.cache;

import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.redis.model.UserCache;

/**
 * Do with redis cache
 */
public interface UserCacheManager {
    /**
     * store user temporarily in redis cache
     * @param userCache
     */
    void storeUserCache(UserCache userCache);

    /**
     * verify user in redis cache
     * @param id
     * @param otp
     * @param secret
     * @return
     * @throws NotFoundException
     * @throws ValidationException
     */
    UserCache verifyUserCache(String id, String otp, String secret) throws NotFoundException, ValidationException;

    /**
     * clear redis cache (make redis cache empty)
     * @param id
     */
    void clearUserCache(String id);
}
