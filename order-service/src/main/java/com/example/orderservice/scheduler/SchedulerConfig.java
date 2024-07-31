package com.example.orderservice.scheduler;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@Getter
public class SchedulerConfig {
    public static final String CRON_JOB_DEFAULT = "0 0 0 * * ?";

    private String dailyHouseKeepingJob;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        dailyHouseKeepingJob = environment.getProperty("job.housekeeping.default", String.class, CRON_JOB_DEFAULT);
    }

    @Bean
    protected TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("task-");
        return threadPoolTaskScheduler;
    }
}
