package com.jieweifu.services.main.impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.User;
import com.jieweifu.services.main.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by 陶Lyn
 * on 2018/3/12.
 */
@Service("newService")
public class UserServiceImpl implements UserService {

    @Autowired
    private DB db;


    @Override
    public void addUser(User user) {
        OperateHandler.assignCreateUser(user);//
        String salt = UUID.randomUUID().toString().replace("-", "");
        System.out.println("=====>" + salt);
        user.setPassword(DigestUtils.md5Hex(salt + user.getPassword()));
        db.insert()
                .save(user)
                .set("salt", salt)
                .execute();
    }

    @Override
    public User findMainUserByUsernameAndPassword(String phone, String password) {
        return db.select()
                .from(User.class)
                .where("mobile_phone= ? ", phone)
                .where("password =MD5(CONCAT(salt,?))", password)
                .queryForEntity(User.class);

    }

    @Override
    public User findById(Integer id) {
        return db.select()
                .from(User.class)
                .where("id= ?", id)
                .queryForEntity(User.class);
    }

    @Override
    public void updatePassword(String password, Integer id) {
        String salt = UUID.randomUUID().toString().replace("-", "");
        password = DigestUtils.md5Hex(salt + password);
        db.update()
                .table(User.class)
                .set("password", password)
                .set("salt",salt)
                .where("id= ?", id)
                .execute();
    }


    @Override
    public String getSalt(int id) {
        return db.select()
                .columns("salt")
                .from(User.class)
                .where("id=?", id)
                .queryForObject(String.class);
    }

    @Override
    public String sendSMSCode() {
        //goto   这里先做一个简单的测试案列;
        return "123456";
    }

    @Override
    public void addPicUrl(int id, String picUrl) {
        User u=new User();
        u.setId(id);
        u.setHeadImgUrl(picUrl);
        db.update()
               .save(u)
                .execute();
    }

    @Override
    public int updateUser(User u) {
        return db.update()
                .save(u)
                .execute();
    }
}
