package com.jieweifu.controllers.main;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.FileUtil;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.common.utils.TokenUtil;
import com.jieweifu.constants.UserConstant;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.User;
import com.jieweifu.services.main.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RedisUtil redisUtil;


    //注册
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        try {
            mainUserService.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setMessage("注册失败");
        }
        return new Result().setMessage("注册成功");
    }

    //登陆  登陆成功后返回Token(加密后的用户id)  每次请求都需要带上此Token
    @PostMapping("/login")
    public Result login(@RequestBody LoginUser mainUser) {

        User user = mainUserService.findMainUserByUsernameAndPassword(mainUser.getMobliePhone(), mainUser.getPassword());
        if(user!=null){
            Map<String,String> userInfo=new WeakHashMap<>();
            userInfo.put(UserConstant.USER_TOKEN,tokenUtil.generateToken(String.valueOf(user.getId())));
            redisUtil.setEX("userName",user.getName(),120, TimeUnit.MINUTES);
            return new Result().setData(userInfo);
        }
        return new Result().setError("用户名或密码错误");
    }

    //修改密码
    @PutMapping("password/update")
    public Result update(@RequestBody String newPassword) {
        int userId = BaseContextHandler.getUserId();
        try {
            mainUserService.updatePassword(newPassword, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setMessage("修改失败");
        }
        return new Result().setMessage("密码修改成功");
    }

    @Value("${custom.upload.home}")
    private String uploadPath;
    /**
     * 修改头像
     */
    @PutMapping("headImage/update")
    public Result updateHeadImg(@RequestParam(value = "file")MultipartFile mFile){
        int userId = BaseContextHandler.getUserId();
        String name= System.currentTimeMillis()+"";
        File file=null;
        try {
            file= FileUtil.uploadFile(mFile,uploadPath,name);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setMessage("上次图片失败");
        }
        String filePath=file.getAbsolutePath();
        mainUserService.addPicUrl(userId,filePath);
        return new Result().setData(filePath);
    }

    /**
     * 修改用户信息
     * */
    @PutMapping("/user/update")
    public Result updateUser(@RequestBody User user){
        int userId = BaseContextHandler.getUserId();
        user.setId(userId);
        int i= mainUserService.updateUser(user);
        if(i>0){
            return new Result().setMessage("更新成功");
        }
        return new Result().setMessage("更新失败");
    }

    /**
     *
     * 短信验证接口  这里先只是测试用
     * */
    @RequestMapping("/sendSMS")
    public String sendSMS(){
        return mainUserService.sendSMSCode();
    }

    /**
     * 登陆使用的临时对象
     */
    public static class LoginUser {
        @NotNull(message = "用户名不能为空")
        private String loginName;

        private String mobliePhone;


        @NotNull(message = "密码不能为空")
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


        public String getMobliePhone() {
            return mobliePhone;
        }

        public void setMobliePhone(String mobliePhone) {
            this.mobliePhone = mobliePhone;
        }
    }

}
