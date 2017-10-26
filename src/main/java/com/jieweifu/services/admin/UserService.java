package com.jieweifu.services.admin;

import com.jieweifu.models.admin.UserModel;

@SuppressWarnings("unused")
public interface UserService {
    void getUser();
    int addUser(UserModel userModel);
    UserModel getUserByUserName(String userName, String password);
    UserModel getUserById(int id);
}
