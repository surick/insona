package com.jieweifu.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.Executors;

@Configuration
public class ScheduleConfig {

    @Bean
    public TaskScheduler taskExecutor() {
        return new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(10));
    }
}
