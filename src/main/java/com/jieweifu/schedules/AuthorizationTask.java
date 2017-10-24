package com.jieweifu.schedules;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationTask {

    int i = 0;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void refreshAuthorization(){
        System.out.println(++i);
    }
}
