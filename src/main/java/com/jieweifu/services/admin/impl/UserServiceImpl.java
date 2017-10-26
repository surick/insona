package com.jieweifu.services.admin.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.UserModel;
import com.jieweifu.services.admin.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private DB db;

    public UserServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public void getUser() {
        List<UserModel> userModelList = db.select().from(UserModel.class).queryForList(UserModel.class);
        userModelList.forEach(userModel -> System.out.println(userModel.getUserName()));
    }

    @Override
    public int addUser(UserModel userModel) {
        return 0;
    }

    @Override
    public UserModel getUserByUserName(String userName, String password) {
        return db.select()
                .columns("id")
                .from(UserModel.class)
                .where("user_name = ?", userName)
                .where("password = MD5(CONCAT(salt, ?))", "123456")
                .queryForEntity(UserModel.class);
    }

    @Override
    public UserModel getUserById(int id) {
        return db.select()
                .from(UserModel.class)
                .where("id = ?", id)
                .queryForEntity(UserModel.class);
    }
}
