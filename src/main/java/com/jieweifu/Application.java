package com.jieweifu;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.models.admin.MenuModel;
import com.jieweifu.models.admin.UserModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        MenuModel menuModel = new MenuModel();
        OperateHandler.assignCreateUser(menuModel);

        UserModel userModel = new UserModel();
        OperateHandler.assignCreateUser(userModel);

        SpringApplication.run(Application.class, args);
    }
}
