package com.jieweifu.controllers.main;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.*;
import com.jieweifu.constants.UserConstant;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.services.main.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
@RestController("mainUser")
@RequestMapping("main/user")
@AdminAuthAnnotation
public class UserController {

    private AppUserService appUserService;
    private TokenUtil tokenUtil;
    private RedisUtil redisUtil;
    private TokenIdUtil tokenIdUtil;

    @Autowired
    public UserController(AppUserService appUserService, TokenUtil tokenUtil, RedisUtil redisUtil, TokenIdUtil tokenIdUtil) {
        this.appUserService = appUserService;
        this.tokenUtil = tokenUtil;
        this.redisUtil = redisUtil;
        this.tokenIdUtil = tokenIdUtil;
    }

    //注册
    @AdminAuthAnnotation(check = false)
    @PostMapping("/register")
    public Result register(@RequestBody RegisterUser registerUser) {

        String phone = registerUser.getPhone();
        String password = registerUser.getPassword();
        String code = registerUser.getCode();


        int i = appUserService.findByPhone(phone);
        if (i > 0) {
            return new Result().setError("号码已存在");
        }
         if (!code.equals(redisUtil.get(phone))) {
       // if (!code.equals("1234")) {
            return new Result().setError("手机验证码错误");
        }
        InsonaUser insonaUser = new InsonaUser();
        insonaUser.setPhone(phone);
        insonaUser.setPassword(password);
        try {
            appUserService.addUser(insonaUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("注册失败");
        }
        int newUserId = appUserService.findIdByPhone(phone).getId();
        Map<String, String> userInfo = new WeakHashMap<>();
        //通过token 封装后的  id
        String tokenId = tokenUtil.generateToken(String.valueOf(newUserId));
        userInfo.put(UserConstant.USER_TOKEN, tokenId);
        redisUtil.setEX(tokenId, tokenId, 30, TimeUnit.DAYS);
        Result result = new Result();
        return new Result().setData(userInfo);
    }

    //登陆  登陆成功后返回Token(加密后的用户id)  每次请求都需要带上此Token
    @AdminAuthAnnotation(check = false)
    @PostMapping("/login")
    public Result login(@RequestBody LoginUser mainUser) {

        InsonaUser user = appUserService.findMainUserByUsernameAndPassword(mainUser.getPhone(), mainUser.getPassword());
        if (user != null) {
            Map<String, String> userInfo = new WeakHashMap<>();
            String tokenId = tokenUtil.generateToken(String.valueOf(user.getId()));
            userInfo.put(UserConstant.USER_TOKEN, tokenId);
            redisUtil.setEX(tokenId, tokenId, 30, TimeUnit.DAYS);
            return new Result().setData(userInfo);
        }
        return new Result().setError("用户名或密码错误");
    }

    //根据用户id查询用户信息

    @GetMapping("/getUser")
    public Result findById(HttpServletRequest request) {
        Integer id = tokenIdUtil.getUserId(request);
        if (id == -1) {
            return new Result().setError(401, "登录超时，重新登录");
        }
        return new Result().setData(appUserService.findById(id));
    }
    //修改密码
    @PutMapping("password/update")
    public Result update(@RequestBody UpdateUser updateUser, HttpServletRequest request) {
        Integer id = tokenIdUtil.getUserId(request);
        if (id == -1) {
            return new Result().setError(401, "登录超时，重新登录");
        }
        InsonaUser insonaUser = appUserService.findById(id);
        if (!insonaUser.getPhone().equals(updateUser.getPhone())) {
            return new Result().setError("手机号码输入错误");
        }
        String code = updateUser.getCode();
        if (!code.equals(redisUtil.get(updateUser.getPhone()))) {
      //  if (!code.equals("1234")) {
            return new Result().setError("手机验证码错误");
        }
        try {
            appUserService.updatePassword(updateUser.getNewPassword(), id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("修改失败");
        }
        return new Result().setMessage("密码修改成功");
    }

    //忘记密码  通过手机号 来判断用户是否存在  如果存在 就发送验证码
    @PutMapping("/password/forget")
    @AdminAuthAnnotation(check = false)
    public Result  forget(@RequestBody UpdateUser updateUser){
        String  phone=updateUser.getPhone();
        InsonaUser user=appUserService.findIdByPhone(phone);

        if (user==null) {
            return new Result().setError("对不起，手机号码不存在");
        }
        Integer id =user.getId();
        String code = updateUser.getCode();
        if (!code.equals(redisUtil.get(phone))) {
             // if (!code.equals("1234")) {
            return new Result().setError("手机验证码错误");
        }
        try {
            appUserService.updatePassword(updateUser.getNewPassword(), id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("修改失败");
        }
        return new Result().setMessage("密码重新设置成功");

    }




    @Value("${custom.upload.home}")
    private String uploadPath;

    /**
     * 修改头像
     *//*
    @PutMapping("headImage/update")
    public Result updateHeadImg(@RequestParam(value = "file") MultipartFile mFile, HttpServletRequest request) {
        Integer id = tokenIdUtil.getUserId(request);
        if (id == -1) {
            return new Result().setError(401, "登录超时，重新登录");
        }
        String name = System.currentTimeMillis() + "";
        File file = null;
        try {
            file = FileUtil.uploadFile(mFile, uploadPath, name);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("上次图片失败");
        }
        String filePath = file.getAbsolutePath();
        appUserService.addPicUrl(id, filePath);
        return new Result().setData(filePath);
    }*/

    /**
     * 修改用户信息
     */
    @PutMapping("/user/update")
    public Result updateUser(@RequestBody InsonaUser insonaUser, HttpServletRequest request) {
        Integer id = tokenIdUtil.getUserId(request);
        if (id == -1) {
            return new Result().setError(401, "登录超时，重新登录");
        }
        insonaUser.setId(id);
        int i = appUserService.updateUser(insonaUser);
        if (i > 0) {
            return new Result().setMessage("更新成功");
        }
        return new Result().setError("更新失败");
    }

    /**
     * 短信验证接口
     */

    private static final int TIME_OUT = 2;
    private static final String TEMPLATE_ID = "1";

    @PostMapping("/sendSMS")
    @AdminAuthAnnotation(check = false)
    public Result sendSMS(@RequestBody LoginUser userPhone) {
        String phone = userPhone.getPhone();
        String code = SMSUtil.getVerificationCode();
        //数组参数，第一个是验证码，第二个是失效时间
        String[] content = {code, String.valueOf(TIME_OUT)};
        //发送
        boolean flag = CCPRESTSmsUtil.sendSMSByYunXunTong(phone, TEMPLATE_ID, content);
        if (flag) {
            redisUtil.setEX(phone, code, TIME_OUT, TimeUnit.MINUTES);
            return new Result().setData(code);
        }
        return new Result().setError("发送失败,网络繁忙");

    }

    /**
     * 登陆使用的临时对象
     */


    public static class LoginUser {


        private String phone;


        @NotNull(message = "密码不能为空")
        private String password;


        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


    /**
     * 注册使用的临时对象
     */
    public static class RegisterUser {
        private String phone;

        private String password;

        private String code;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    /**
     * 修改密码专用类
     */
    public static class UpdateUser {

        private String phone;
        private String newPassword;

        private String code;


        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
