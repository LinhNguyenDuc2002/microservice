package com.example.userservice.scheduler;

import com.example.userservice.config.AsyncTaskConfig;
import com.example.userservice.service.HouseKeepingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("!test")
public class SchedulerManager {
    @Autowired
    private SchedulerConfig schedulerConfig;

    @Autowired
    private HouseKeepingService houseKeepingService;

    @Autowired
    @Qualifier(SchedulerConfig.SCHEDULER_BEAN)
    private TaskScheduler taskScheduler;

    @Autowired
    @Qualifier(AsyncTaskConfig.BEAN_ASYNC_EXECUTOR)
    private TaskExecutor taskExecutor;

    public void startHouseKeepingJob() { //create scheduled jobs
        CronTrigger cronTrigger;

        try {
            cronTrigger = new CronTrigger(schedulerConfig.getDailyHouseKeepingJob());
        } catch (Exception e) {
            log.error("Invalid cron expression: {}", schedulerConfig.getDailyHouseKeepingJob());
            cronTrigger = new CronTrigger(SchedulerConfig.CRON_JOB_DEFAULT);
        }

        taskScheduler.schedule(() -> {
        }, cronTrigger);

        log.info("Created scheduler for housekeeping: {}", cronTrigger.getExpression());
    }
}
