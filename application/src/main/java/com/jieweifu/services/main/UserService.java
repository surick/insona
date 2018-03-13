package com.jieweifu.services.main;

import com.jieweifu.models.admin.User;

/**
 * Created by 陶Lyn
 * on 2018/3/12.
 */
public interface UserService {

    //注册
    void addUser(User user);

    //登陆
    User findMainUserByUsernameAndPassword(String phone,String password);

    //查询当前用户
    User findById(Integer id);

    //修改密码
    void  updatePassword(String password,Integer id);

    //注册 修改密码  忘记密码  发送的手机验证码
    String sendSMSCode();

    //得到料 用于修改密码用
    String  getSalt(int id);

    //保存图片地址
    void addPicUrl(int id,String picUrl);

    //修改用户信息
    int updateUser(User u);
}
