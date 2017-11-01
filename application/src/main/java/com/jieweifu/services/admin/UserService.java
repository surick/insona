package com.jieweifu.services.admin;

import com.jieweifu.models.MenuElements;
import com.jieweifu.models.admin.Element;
import com.jieweifu.models.admin.User;

import java.util.List;

@SuppressWarnings("unused")
public interface UserService {
    List<User> getAllUsers();

    int addUser(User user);

    User doUserLogin(String userName, String password);

    boolean doUserLogin(int userId, String password);

    User getUserById(int id);

    void updateUserHeadImg(int userId, String headImgUrl);

    void updateUser(User user);

    void updateUserPassword(int userId, String password);

    boolean getIsAdmin(int userId);

    MenuElements getMenuElements(int userId, boolean isAdmin);

    MenuElements getAllMenuElements();

    List<Element> getAllAuthElements(int userId, boolean isAdmin);

    List<User> getUsersByPage(int pageIndex, int pageSize);

    User getUserByUserName(String userName);

    void deleteUser(int id);

    int getUserTotal();

    void isDelete(int id);
}
