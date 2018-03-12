package com.jieweifu.services.main;

import com.jieweifu.models.admin.User;
import com.jieweifu.models.main.MainUser;

/**
 * Created by 陶Lyn
 * on 2018/3/12.
 */
public interface MainUserService {

    //注册
    void addMainUser(User user);

    //登陆
    MainUser findMainUserByUsernameAndPassword(String phone,String password);

    //查询当前用户
    MainUser findById(Integer id);

    //修改密码
    void  updatePassword(String password,Integer id);
}
