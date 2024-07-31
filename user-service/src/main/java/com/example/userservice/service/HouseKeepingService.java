package com.example.userservice.service;

/**
 * executor
 */
public interface HouseKeepingService {
    /**
     * Clean expired user cache
     */
    void cleanUserCache();
}
