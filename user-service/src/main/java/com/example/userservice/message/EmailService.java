package com.example.userservice.message;

import com.example.userservice.config.AsyncTaskConfig;
import org.springframework.scheduling.annotation.Async;

/**
 * Handle sending email
 * @param <T>
 */
public interface EmailService<T extends BaseMessage> {
    /**
     * @param message
     */
    @Async(AsyncTaskConfig.BEAN_ASYNC_EXECUTOR)
    void sendMessage(T message);
}
