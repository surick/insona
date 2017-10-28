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
     * @return 返回用户登录成功的Token, 每次请求头需带上此Token, 校验权限
     */
    @AdminAuthAnnotation(check = false)
    @PostMapping("login")
    @ResponseBody
    public ResultModel doLogin(@RequestBody LoginInfo loginInfo) {
        String loginName = loginInfo.loginName;
        String password = loginInfo.password;
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
     * @return 更新Token后原Token失效, 返回用户的新token
     */
    @GetMapping("token/refresh")
    @ResponseBody
    public ResultModel refreshToken() {
        String userId = String.valueOf(BaseContextHandler.getUserId());
        boolean isAdmin = BaseContextHandler.getUserIsAdmin();
        tokenUtil.refreshAuthorization(Integer.parseInt(userId), isAdmin);
        return new ResultModel().setData(tokenUtil.generateToken(userId));
    }

    @PutMapping("headImage/update")
    @ResponseBody
    public ResultModel updateHeadImg(@RequestBody String headImgUrl) {
        int userId = BaseContextHandler.getUserId();
        userService.updateUserHeadImg(userId, headImgUrl);
        return new ResultModel().setData(null);
    }

    /**
     * @return 更新用户密码
     */
    @PutMapping("password/update")
    @ResponseBody
    public ResultModel updatePassword(@RequestBody PasswordInfo passwordInfo) {
        int userId = BaseContextHandler.getUserId();
        String oldPassword = passwordInfo.oldPassword;
        String newPassword = passwordInfo.newPassword;
        UserModel userModel = userService.doUserLogin(userId, oldPassword);
        if (userModel != null) {
            userService.updateUserPassword(userId, newPassword);
            return new ResultModel().setMessage("密码更新成功");
        }
        return new ResultModel().setError("密码错误");
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

    /**
     * loginInfo类
     */
    public static class LoginInfo {
        private String loginName;
        private String password;

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * passwordInfo类
     */
    public static class PasswordInfo {
        private String oldPassword;
        private String newPassword;

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
