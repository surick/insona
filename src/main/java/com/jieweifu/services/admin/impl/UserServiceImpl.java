package com.jieweifu.services.admin.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.services.admin.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private DB db;

    public UserServiceImpl(DB db){
        this.db = db;
    }

    @Override
    public void getUser() {
        //db.select().from("user").queryForList();
        System.out.println(db.select().from("user").where("id = ? AND name = ?", "2", "dsd").total());
    }
}
