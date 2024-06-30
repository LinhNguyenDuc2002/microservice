package com.example.userservice.scheduler;

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
    public static final String SCHEDULER_BEAN = "SCHEDULER_BEAN";

    public static final String CRON_JOB_DEFAULT = "0 0 0 * * ?"; //seconds - minutes - hours - dayOfMonth - month - dayOfWeek

    private String dailyHouseKeepingJob;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        dailyHouseKeepingJob = environment.getProperty("job.housekeeping.default", String.class, CRON_JOB_DEFAULT);
    }

    @Bean(SCHEDULER_BEAN)
    protected TaskScheduler tclStateSyncScheduler() {
        //Create an object to schedule task
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        //Allow 10 threads to run at the same time
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("task-");
        return threadPoolTaskScheduler;
    }
}
