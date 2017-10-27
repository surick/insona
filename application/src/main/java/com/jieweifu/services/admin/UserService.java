package com.jieweifu.services.admin;

import com.jieweifu.models.admin.ElementModel;
import com.jieweifu.models.admin.UserModel;
import com.jieweifu.vo.admin.MenuElement;

import java.util.List;

@SuppressWarnings("unused")
public interface UserService {
    void getUser();
    int addUser(UserModel userModel);
    UserModel doUserLogin(String userName, String password);
    UserModel getUserById(int id);
    void updateUserHeadImg(int userId, String headImgUrl);
    void updateUser(UserModel userModel);
    boolean getIsAdmin(int userId);
    List<MenuElement> getMenuElements(int userId, boolean isAdmin);
    List<MenuElement> getAllMenuElements();
    List<ElementModel> getAllAuthElements(int userId, boolean isAdmin);
}
