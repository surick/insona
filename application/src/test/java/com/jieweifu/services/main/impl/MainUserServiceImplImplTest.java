package com.jieweifu.services.main.impl;

import com.jieweifu.models.main.MainUser;
import com.jieweifu.services.main.MainUserService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by 陶Lyn
 * on 2018/3/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainUserServiceImplTest {

    private Logger logger=Logger.getLogger(MainUserServiceImplTest.class);
    @Autowired
    private MainUserService mainUserService;

    @Test
    public void addMainUser() throws Exception {
        MainUser user=new MainUser();
        user.setPhone("13012341234");
        user.setPassword("123456");
        mainUserService.addMainUser(user);
    }

    @Test
    public void findMainUserByUsernameAndPassword() throws Exception {
        MainUser mainUser=mainUserService.findMainUserByUsernameAndPassword("15012341234","123456");
        logger.info(mainUser.getBirthday()+"====="+mainUser.getRealName()+"======"+mainUser.getUserName()+"===123"+mainUser.getPassword());
    }

    @Test
    public void findById() throws Exception {
        MainUser mainUser=mainUserService.findById(1);
        logger.info(mainUser.getBirthday()+"====="+mainUser.getRealName()+"======"+mainUser.getUserName()+"===123"+mainUser.getPassword());
    }

    @Test
    public void updatePassword() throws Exception {
        mainUserService.updatePassword("123123",1);
    }

}