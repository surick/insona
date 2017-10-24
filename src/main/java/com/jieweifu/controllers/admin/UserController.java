package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.ResultModel;
import com.jieweifu.services.admin.UserService;
import com.jieweifu.common.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@Controller("AdminUser")
@RequestMapping("sys/user")
@AdminAuthAnnotation
public class UserController {
    private UserService userService;
    private TokenUtil tokenUtil;

    @Autowired
    public UserController(UserService userService, TokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    /**
     * @param loginName 用户名
     * @param password  密码
     * @return 返回用户登录成功的Token, 每次请求头需带上此Token, 校验权限
     */
    @AdminAuthAnnotation(check = false)
    @PostMapping("login")
    @ResponseBody
    public ResultModel doLogin(String loginName, String password) {
        return new ResultModel().setData(null);
    }

    /**
     * 更新Token后原Token失效
     * @return 返回用户的新token
     */
    @GetMapping("token/refresh")
    @ResponseBody
    public ResultModel refreshToken(@RequestAttribute("user") String user) {
        return new ResultModel();
    }

    /**
     * @return 返回用户功能权限
     */
    @GetMapping("power")
    @ResponseBody
    public ResultModel getUserPower() {
        return null;
    }


}
