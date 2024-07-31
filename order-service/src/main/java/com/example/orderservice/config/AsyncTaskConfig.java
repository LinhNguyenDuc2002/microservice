package com.example.orderservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncTaskConfig {
    public static final String BEAN_ASYNC_EXECUTOR = "APP_ASYNC_EXECUTOR";

    private static final int DEFAULT_POOL_SIZE = 10;

    @Autowired
    private Environment environment;

    private int corePoolSize;

    private int maxPoolSize;

    private int queueCapacity;

    @PostConstruct
    public void init() {
        corePoolSize = environment.getProperty("executor.corePoolSize", Integer.class, DEFAULT_POOL_SIZE);
        maxPoolSize = environment.getProperty("executor.maxPoolSize", Integer.class, Integer.MAX_VALUE);
        queueCapacity = environment.getProperty("executor.queueCapacity", Integer.class, Integer.MAX_VALUE);
    }

    @Bean(BEAN_ASYNC_EXECUTOR)
    public AsyncTaskExecutor initTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setThreadNamePrefix("task-");

        return threadPoolTaskExecutor;
    }

    /**
     * Using this executor to delegate the security context between threads
     *
     * @param executor
     * @return
     */
    @Bean
    public DelegatingSecurityContextAsyncTaskExecutor delegateSecurityContextAsyncTaskExecutor(@Qualifier(BEAN_ASYNC_EXECUTOR) AsyncTaskExecutor executor) {
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
