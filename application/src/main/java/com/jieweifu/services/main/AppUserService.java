package com.jieweifu.services.main;

import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.InsonaUser;

/**
 * Created by 陶Lyn
 * on 2018/3/12.
 */
public interface AppUserService {

    //注册

    void addUser(InsonaUser insonaUser);

    //登陆
    InsonaUser findMainUserByUsernameAndPassword(String phone,String password);

    //查询当前用户
    InsonaUser findById(Integer id);

    //修改密码
    void  updatePassword(String password,Integer id);

    //注册 修改密码  忘记密码  发送的手机验证码
    String sendSMSCode();

    //得到料 用于修改密码用
    String  getSalt(int id);

    //保存图片地址
    void addPicUrl(int id,String picUrl);

    //修改用户信息
    int updateUser(InsonaUser insonaUser);

    //手机号码注册判断 号码是否存在
    int findByPhone(String phone);

    //通过手机号码 查询id
    InsonaUser findIdByPhone(String phone);
}
