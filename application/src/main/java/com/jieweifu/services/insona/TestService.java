package com.jieweifu.services.insona;

import com.jieweifu.models.insona.InsonaUser;

import java.util.List;

/**
 * Created by 10173 on 2018/3/12.
 */
public interface TestService {
    //测试1 通过id查询用户
    InsonaUser findInsonaUserByid(int id);

    //测试2 查询所有用户
    List<InsonaUser> findInsonaUserAll();

    //测试3  添加用户
    void addInsoNaUser(InsonaUser insonaUser);


    //测试4 修改用户
    void  updateInsoNaUser(InsonaUser insonaUser);

    //测试5  通过id删除用户
    void  deleteInsonaUserById(int id );
}
