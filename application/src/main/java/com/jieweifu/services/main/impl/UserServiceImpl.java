package com.jieweifu.services.main.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.main.MainUser;
import com.jieweifu.services.main.MainUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 陶Lyn
 * on 2018/3/12.
 */
@Service
public class MainUserServiceImpl implements MainUserService {

    @Autowired
    private DB db;


    @Override
    public void addMainUser(MainUser mainUser) {
        db.insert()
                .save(mainUser)
                .execute();
    }

    @Override
    public MainUser findMainUserByUsernameAndPassword(String phone, String password) {
        return db.select()
                .from(MainUser.class)
                .where("phone= ? And password = ?",phone,password)
                .queryForEntity(MainUser.class);

    }

    @Override
    public MainUser findById(Integer id) {
        return db.select()
                .from(MainUser.class)
                .where("id= ?" ,id)
                .queryForEntity(MainUser.class);
    }

    @Override
    public void updatePassword(String password,Integer id) {
        db.update()
                .table(MainUser.class)
                .set("password",password)
                .where("id= ?" ,id)
                .execute();
    }
}
