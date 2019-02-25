package com.jieweifu.controllers.main;

import com.jieweifu.common.utils.*;
import com.jieweifu.constants.UserConstant;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.services.main.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
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

    /**
     * 注册
     * @param registerUser
     * @return
     */
    @AdminAuthAnnotation(check = false)
    @PostMapping("/register")
    public Result register(@RequestBody RegisterUser registerUser) {

        String password = registerUser.getPassword();
        String code = registerUser.getCode();
        String gizwitsUsername = registerUser.getGizwitsUsername();
        String gizwitsPassword = registerUser.getGizwitsPassword();
        String phone = registerUser.getPhone();
        String email = registerUser.getEmail();

        InsonaUser insonaUser = new InsonaUser();

        if (registerUser.getType() == 0) {
            int i = appUserService.findByPhone(phone);

            if (i > 0) {
                return new Result().setError("号码已存在");
            }

            if (!code.equals(redisUtil.get(phone))) {
                return new Result().setError("手机验证码错误");
            }
            insonaUser.setPhone(phone);
        }

        if (registerUser.getType() == 1) {
            int i = appUserService.findByEmail(email);

            if (i > 0) {
                return new Result().setError("邮箱已存在");
            }

            if (!code.equals(redisUtil.get(email))) {
                return new Result().setError("验证码错误");
            }
            insonaUser.setEmail(email);
        }

        insonaUser.setPassword(password);
        insonaUser.setGizwitsUsername(gizwitsUsername);
        insonaUser.setGizwitsPassword(gizwitsPassword);

        try {
            appUserService.addUser(insonaUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("注册失败");
        }

        int newUserId = registerUser.getType() == 0 ?
                appUserService.findIdByPhone(phone).getId() : appUserService.findIdByEmail(email).getId();
        Map<String, String> userInfo = new WeakHashMap<>();
        //通过token封装后的id
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
        InsonaUser user = new InsonaUser();
        switch (mainUser.getType()) {
            case 0:
                user = appUserService.findMainUserByUsernameAndPassword(mainUser.getPhone(), mainUser.getPassword());
                break;
            case 1:
                user = appUserService.findMainUser(mainUser.getEmail(), mainUser.getPassword());
                break;
            default:
                break;
        }
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

    //根据token得到对应的机智云信息
    @GetMapping("/getGizwits")
    public Result findGizwitsById(HttpServletRequest request){
        Integer id = tokenIdUtil.getUserId(request);
        if (id == -1) {
            return new Result().setError(401, "登录超时，重新登录");
        }
        Map<String,String> map=new HashMap<>();
        map.put("gizwits_username",appUserService.findById(id).getGizwitsUsername());
        map.put("gizwits_password",appUserService.findById(id).getGizwitsPassword());
        return new Result().setData(map);
    }

    //修改密码
    @PutMapping("password/update")
    public Result update(@RequestBody UpdateUser updateUser, HttpServletRequest request) {
        Integer id = tokenIdUtil.getUserId(request);
        String code = updateUser.getCode();
        if (id == -1) {
            return new Result().setError(401, "登录超时，重新登录");
        }
        InsonaUser insonaUser = appUserService.findById(id);

        if (updateUser.getType() == 0) {
            if (!insonaUser.getPhone().equals(updateUser.getPhone())) {
                return new Result().setError("手机号码输入错误");
            }
            if (!code.equals(redisUtil.get(updateUser.getPhone()))) {
                return new Result().setError("手机验证码错误");
            }
        }
        if (updateUser.getType() == 1) {
            if (!insonaUser.getEmail().equals(updateUser.getEmail())) {
                return new Result().setError("邮箱输入错误");
            }
            if (!code.equals(redisUtil.get(updateUser.getEmail()))) {
                return new Result().setError("邮箱验证码错误");
            }
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
    public Result  forget(@RequestBody UpdateUser updateUser) {
        InsonaUser user = new InsonaUser();
        String code = updateUser.getCode();

        if (updateUser.getType() == 0) {
            String phone=updateUser.getPhone();
            user=appUserService.findIdByPhone(phone);

            if (user==null) {
                return new Result().setError("对不起，手机号码不存在");
            }
            if (!code.equals(redisUtil.get(phone))) {
                return new Result().setError("手机验证码错误");
            }
        }

        if (updateUser.getType() == 1) {
            String email = updateUser.getEmail();
            user = appUserService.findIdByEmail(email);

            if (user == null) {
                return new Result().setError("对不起，邮箱不存在");
            }
            if (!code.equals(redisUtil.get(email))) {
                return new Result().setError("邮箱验证码错误");
            }
        }

        Integer id =user.getId();
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
    private static final String TEMPLATE_ID = "403000";

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
            return new Result().setMessage("发送成功");
//            return new Result().setData(code);
        }
        return new Result().setError("发送失败,网络繁忙");

    }

    @PostMapping("/sendEmail")
    @AdminAuthAnnotation(check = false)
    public Result sendEmail(@RequestBody LoginUser loginUser) {
        String email = loginUser.getEmail();
        try {
            String code = EmailUtil.sendEmailCode(email);
            if (!code.equals("-1")) {
                redisUtil.setEX(email, code, TIME_OUT, TimeUnit.MINUTES);
                return new Result().setMessage("发送成功");
            }
        } catch (Exception e) {
            return new Result().setError("发送失败");
        }
        return new Result().setMessage("发送成功");
    }

    /**
     * 登陆使用的临时对象
     */
    public static class LoginUser {

        private String phone;

        private String email;

        /**
         * 0-手机
         * 1-邮箱
         */
        private Integer type;

        @NotNull(message = "密码不能为空")
        private String password;


        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
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
        private String email;

        /**
         * 0-手机
         * 1-邮箱
         */
        private Integer type;

        private String phone;

        private String password;

        private String code;

        private String gizwitsUsername;

        private String gizwitsPassword;

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

        public String getGizwitsUsername() {
            return gizwitsUsername;
        }

        public void setGizwitsUsername(String gizwitsUsername) {
            this.gizwitsUsername = gizwitsUsername;
        }

        public String getGizwitsPassword() {
            return gizwitsPassword;
        }

        public void setGizwitsPassword(String gizwitsPassword) {
            this.gizwitsPassword = gizwitsPassword;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

    /**
     * 修改密码专用类
     */
    public static class UpdateUser {

        private String email;
        /**
         * 0-手机
         * 1-邮件
         */
        private Integer type;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

}
