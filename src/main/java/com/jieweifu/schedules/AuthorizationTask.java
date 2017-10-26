package com.jieweifu.schedules;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationTask {
    @Scheduled(cron = "0 0/1 * * * ?")
    public void refreshAuthorization(){

    }
}
