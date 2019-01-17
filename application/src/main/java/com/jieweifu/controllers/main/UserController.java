package com.jieweifu.controllers.main;

import com.jieweifu.common.utils.*;
import com.jieweifu.constants.UserConstant;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.models.wechat.OAuth2AccessToken;
import com.jieweifu.models.wechat.WeixinUserInfo;
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

    @AdminAuthAnnotation(check = false)
    @GetMapping("/wx/login")
    public Result wxLogin(@RequestParam(name = "code") String code,
                          @RequestParam(name = "state", required = false) String state) {
        OAuth2AccessToken oAuth2AccessToken = WechatUtil.getAccessToken(code);
        if (oAuth2AccessToken == null) {
            return new Result().setError("获取微信用户信息失败");
        }
        WeixinUserInfo weixinUserInfo =
                WechatUtil.getWeixinUserInfo(oAuth2AccessToken.getAccess_token(), oAuth2AccessToken.getOpenid());

        InsonaUser user = appUserService.findByOpenId(weixinUserInfo.getOpenid());
        if (user != null) {
            // 已用微信注册
            Map<String, String> userInfo = new HashMap<>(3);

            String tokenId = tokenUtil.generateToken(String.valueOf(user.getId()));
            userInfo.put(UserConstant.USER_TOKEN, tokenId);

            redisUtil.setEX(tokenId, tokenId, 30, TimeUnit.DAYS);

            return new Result().setData(userInfo);
        } else {
            // 第一次使用微信登录
            return new Result().setData(301, weixinUserInfo);
        }
    }


    /**
     * 注册
     * @param registerUser
     * @return
     */
    @AdminAuthAnnotation(check = false)
    @PostMapping("/register")
    public Result register(@RequestBody RegisterUser registerUser) {

        String phone = registerUser.getPhone();
        String password = registerUser.getPassword();
        String code = registerUser.getCode();
        String gizwitsUsername = registerUser.getGizwitsUsername();
        String gizwitsPassword = registerUser.getGizwitsPassword();

        // 微信相关
        String openId = registerUser.getOpenId();
        String unionId = registerUser.getUnionId();
        String nickName = registerUser.getNickName();
        Integer sex = registerUser.getSex();
        String city = registerUser.getCity();
        String province = registerUser.getProvince();
        String country = registerUser.getCountry();
        String headimgurl = registerUser.getHeadimgurl();

        if (!code.equals(redisUtil.get(phone))) {
            return new Result().setError("手机验证码错误");
        }

        int i = appUserService.findByPhone(phone);
        if (i > 0) {
            // 号码已经注册 重新绑定微信openid
            if (code.equals(redisUtil.get(phone))) {
                InsonaUser updateUser = appUserService.findIdByPhone(phone);

                updateUser.setOpenId(openId);
                updateUser.setUnionId(unionId);
                updateUser.setName(nickName);
                updateUser.setAddress(country + province + city);
                updateUser.setHeadImgUrl(headimgurl);
                switch (sex) {
                    case 0:
                        updateUser.setGender("N");
                        break;
                    case 1:
                        updateUser.setGender("M");
                        break;
                    case 2:
                        updateUser.setGender("F");
                        break;
                    default:
                        updateUser.setGender("");
                        break;
                }
                try {
                    appUserService.updateUser(updateUser);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result().setError("绑定openid失败");
                }
            }
//            return new Result().setError("号码已存在");
        } else {
            // 新注册用户
            InsonaUser insonaUser = new InsonaUser();

            insonaUser.setPhone(phone);
            insonaUser.setPassword(password);
            insonaUser.setGizwitsUsername(gizwitsUsername);
            insonaUser.setGizwitsPassword(gizwitsPassword);
            insonaUser.setOpenId(openId);
            insonaUser.setUnionId(unionId);
            insonaUser.setAddress(country + province + city);
            insonaUser.setName(nickName);
            insonaUser.setHeadImgUrl(headimgurl);
            switch (sex) {
                case 0:
                    insonaUser.setGender("N");
                    break;
                case 1:
                    insonaUser.setGender("M");
                    break;
                case 2:
                    insonaUser.setGender("F");
                    break;
                default:
                    insonaUser.setGender("N");
                    break;
            }

            try {
                appUserService.addUser(insonaUser);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result().setError("注册失败");
            }
        }

        int newUserId = appUserService.findIdByPhone(phone).getId();

        Map<String, String> userInfo = new WeakHashMap<>();

        //通过token封装后的id
        String tokenId = tokenUtil.generateToken(String.valueOf(newUserId));
        userInfo.put(UserConstant.USER_TOKEN, tokenId);

        redisUtil.setEX(tokenId, tokenId, 30, TimeUnit.DAYS);

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
        if (id == -1) {
            return new Result().setError(401, "登录超时，重新登录");
        }
        InsonaUser insonaUser = appUserService.findById(id);
        if (!insonaUser.getPhone().equals(updateUser.getPhone())) {
            return new Result().setError("手机号码输入错误");
        }
        String code = updateUser.getCode();
        if (!code.equals(redisUtil.get(updateUser.getPhone()))) {
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
        private String gizwitsUsername;
        private String gizwitsPassword;

        private String openId;
        private String unionId;
        private String nickName;
        private Integer sex;
        private String city;
        private String province;
        private String country;
        private String headimgurl;

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

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getUnionId() {
            return unionId;
        }

        public void setUnionId(String unionId) {
            this.unionId = unionId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
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
