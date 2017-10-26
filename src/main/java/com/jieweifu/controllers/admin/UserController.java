package com.jieweifu.controllers.admin;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.constants.CommonConstant;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.ResultModel;
import com.jieweifu.models.admin.UserModel;
import com.jieweifu.services.admin.UserService;
import com.jieweifu.common.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@Controller("SystemUser")
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
     * 返回用户登录成功的Token
     * @return 每次请求头需带上此Token, 校验权限
     */
    @AdminAuthAnnotation(check = false)
    @PostMapping("login")
    @ResponseBody
    public ResultModel doLogin(String loginName, String password) {
        UserModel userModel = userService.getUserByUserName(loginName, password);
        if (userModel != null) {
            return new ResultModel().setData(tokenUtil.generateToken(String.valueOf(userModel.getId())));
        }
        return new ResultModel().setError("用户名或密码错误");
    }

    /**
     * 更新Token后原Token失效
     * @return 返回用户的新token
     */
    @GetMapping("token/refresh")
    @ResponseBody
    public ResultModel refreshToken() {
        String userId = String.valueOf(BaseContextHandler.get(CommonConstant.USER_ID));
        return new ResultModel().setData(tokenUtil.generateToken(userId));
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
