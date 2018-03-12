package com.jieweifu.controllers.main;

import com.jieweifu.models.Result;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.main.MainUser;
import com.jieweifu.services.main.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by 陶Lyn
 * on 2018/3/12.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("main/user")
public class UserController {
    @Resource(name = "newService")
    private UserService mainUserService;

    //注册
    @PostMapping("/register")
    public Result register( @RequestBody User user){
        try {
            mainUserService.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setMessage("注册失败");
        }
        return new Result().setMessage("注册成功");
    }

    //修改密码
    @PutMapping("/update")
    public Result  update(int id,String newPassword){
        try {
            mainUserService.updatePassword(newPassword,id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setMessage("修改失败");
        }
        return new Result().setMessage("修改成功");
    }


    //登陆  登陆成功后返回当前用户的数据
    @GetMapping("/login")
    public Result login(@RequestBody User mainUser){
        User user=null;
        try {
            user= mainUserService.findMainUserByUsernameAndPassword(mainUser.getPhone(),mainUser.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setMessage("登陆失败");
        }
        return new Result().setData(user);
    }

}
