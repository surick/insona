package com.jieweifu.interceptors;

import com.jieweifu.schedules.NotiComponent;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 注册监听事件
 */
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
            NotiComponent notiComponent = (NotiComponent) event.getApplicationContext().getBean(NotiComponent.class);
            notiComponent.start();
    }
}
