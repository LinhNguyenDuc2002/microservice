package com.example.userservice.cache;

import com.example.userservice.redis.model.UserCache;

import java.util.Optional;

/**
 * Work with redis cache
 */
public interface UserCacheManager {
    /**
     * Store user temporarily in redis cache
     * @param userCache
     */
    void storeUserCache(UserCache userCache);

    /**
     *
     * @param secret
     * @return
     */
    Optional<UserCache> getUserCache(String secret);

    /**
     * Clear redis cache (make redis cache empty)
     * @param id
     */
    void clearUserCache(String id);
}
