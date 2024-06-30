package com.example.userservice.listener;

import com.example.userservice.scheduler.SchedulerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Slf4j
public class ApplicationStartUpListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private SchedulerManager schedulerManager;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Application started");

    }
}
