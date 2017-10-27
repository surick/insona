package com.jieweifu.controllers.admin;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.TokenUtil;
import com.jieweifu.constants.UserConstant;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.ResultModel;
import com.jieweifu.models.admin.UserModel;
import com.jieweifu.services.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.WeakHashMap;

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
     *
     * @return 每次请求头需带上此Token, 校验权限
     */
    @AdminAuthAnnotation(check = false)
    @PostMapping("login")
    @ResponseBody
    public ResultModel doLogin(@RequestBody Map<String, String> loginInfo) {
        String loginName = loginInfo.get("loginName");
        String password = loginInfo.get("password");
        UserModel userModel = userService.doUserLogin(loginName, password);
        if (userModel != null) {
            tokenUtil.refreshAuthorization(userModel.getId(), userService.getIsAdmin(userModel.getId()));
            Map<String, String> userInfo = new WeakHashMap<>();
            userInfo.put(UserConstant.USER_TOKEN, tokenUtil.generateToken(String.valueOf(userModel.getId())));
            userInfo.put(UserConstant.USER_NAME, userModel.getName());
            userInfo.put(UserConstant.USER_HEAD_IMG, userModel.getHeadImgUrl());
            return new ResultModel().setData(userInfo);
        }
        return new ResultModel().setError("用户名或密码错误");
    }

    /**
     * 更新Token后原Token失效
     *
     * @return 返回用户的新token
     */
    @GetMapping("token/refresh")
    @ResponseBody
    public ResultModel refreshToken() {
        String userId = String.valueOf(BaseContextHandler.getUserId());
        boolean isAdmin = BaseContextHandler.getUserIsAdmin();
        tokenUtil.refreshAuthorization(Integer.parseInt(userId), isAdmin);
        return new ResultModel().setData(tokenUtil.generateToken(userId));
    }

    @PutMapping("head/update")
    @ResponseBody
    public ResultModel updateHeadImg(@RequestBody String headImgUrl) {
        int userId = BaseContextHandler.getUserId();
        userService.updateUserHeadImg(userId, headImgUrl);
        return new ResultModel().setData(null);
    }

    /**
     * @return 返回用户功能权限
     */
    @GetMapping("power")
    @ResponseBody
    public ResultModel getUserPower() {
        int userId = BaseContextHandler.getUserId();
        boolean isAdmin = BaseContextHandler.getUserIsAdmin();
        return new ResultModel().setData(userService.getMenuElements(userId, isAdmin));
    }
}
