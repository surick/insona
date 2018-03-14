package com.jieweifu.controllers.main;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.*;
import com.jieweifu.constants.UserConstant;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.services.main.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@AdminAuthAnnotation
public class UserController {

    @Resource(name = "newService")
    private UserService mainUserService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TokenIdUtil tokenIdUtil;
    //注册
    @PostMapping("/register")
    public Result register(@RequestBody RegisterUser registerUser) {

        String phone = registerUser.getPhone();
        String password=registerUser.getPassword();
        String  code=registerUser.getCode();


        int i = mainUserService.findByPhone(phone);
        if (i > 0) {
            return new Result().setMessage("号码已存在");
        }
        if (!code.equals(redisUtil.get(phone))) {
        //if (!code.equals("1234")) {
            return new Result().setMessage("手机验证码错误");
        }
        InsonaUser insonaUser=new InsonaUser();
        insonaUser.setPhone(phone);
        insonaUser.setPassword(password);
        try {
            mainUserService.addUser(insonaUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setMessage("注册失败");
        }
        int newUserId = mainUserService.findIdByPhone(phone).getId();
        Map<String, String> userInfo = new WeakHashMap<>();
        //通过token 封装后的  id
        String tokenId=tokenUtil.generateToken(String.valueOf(newUserId));
        userInfo.put(UserConstant.USER_TOKEN, tokenId);
        redisUtil.setEX(tokenId, tokenId, 30, TimeUnit.DAYS);
        Result result=new Result();
        return new Result().setData(userInfo);
    }

    //登陆  登陆成功后返回Token(加密后的用户id)  每次请求都需要带上此Token
    @AdminAuthAnnotation(check = false)
    @PostMapping("/login")
    public Result login(@RequestBody LoginUser mainUser) {

        InsonaUser user = mainUserService.findMainUserByUsernameAndPassword(mainUser.getPhone(), mainUser.getPassword());
        if (user != null) {
            Map<String, String> userInfo = new WeakHashMap<>();
            String tokenId=tokenUtil.generateToken(String.valueOf(user.getId()));
            userInfo.put(UserConstant.USER_TOKEN,tokenId );
            redisUtil.setEX(tokenId, tokenId, 30, TimeUnit.DAYS);
            return new Result().setData(userInfo);
        }
        return new Result().setError("用户名或密码错误");
    }

    //根据用户id查询用户信息
    @PostMapping("/getUser")
    public Result findById(@RequestBody HeadToken headToken){
        Integer id=tokenIdUtil.getUserId(headToken.getHeadToken());
        return new Result().setData(mainUserService.findById(id));
    }


    //修改密码
    @PutMapping("password/update")
    public Result update(@RequestBody UpdateUser updateUser) {

        Integer id=tokenIdUtil.getUserId(updateUser.getHeadToken());

        InsonaUser insonaUser=mainUserService.findById(id);
        if(insonaUser==null||!insonaUser.getPhone().equals(updateUser.getPhone())){
            return new Result().setMessage("登陆超时，重新登陆");
        }
        String code=updateUser.getCode();
        if (!code.equals(redisUtil.get(updateUser.getPhone()))) {
            //if (!code.equals("1234")) {
            return new Result().setMessage("手机验证码错误");
        }
        try {
            mainUserService.updatePassword(updateUser.getNewPassword(), id);
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
    public Result updateHeadImg(@RequestParam(value = "file") MultipartFile mFile) {
        int userId = BaseContextHandler.getUserId();
        String name = System.currentTimeMillis() + "";
        File file = null;
        try {
            file = FileUtil.uploadFile(mFile, uploadPath, name);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setMessage("上次图片失败");
        }
        String filePath = file.getAbsolutePath();
        mainUserService.addPicUrl(userId, filePath);
        return new Result().setData(filePath);
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/user/update")
    public Result updateUser(@RequestBody User user) {
        String a=(String)redisUtil.get(UserConstant.USER_ID);
        String b=tokenUtil.getUserId(a);
        Integer id=Integer.parseInt(b);
        user.setId(id);
        int i = mainUserService.updateUser(user);
        if (i > 0) {
            return new Result().setMessage("更新成功");
        }
        return new Result().setMessage("更新失败");
    }

    /**
     * 短信验证接口
     */

    private static final int TIME_OUT = 2;
    private static final String TEMPLATE_ID = "1";

    @PostMapping("/sendSMS")
    public Result sendSMS(@RequestBody LoginUser userPhone) {
       String phone= userPhone.getPhone();
        String code = SMSUtil.getVerificationCode();
        System.out.println("====>"+phone);
        //数组参数，第一个是验证码，第二个是失效时间
        String[] content = {code, String.valueOf(TIME_OUT)};
        //发送
        boolean flag=CCPRESTSmsUtil.sendSMSByYunXunTong(phone, TEMPLATE_ID, content);
        if (flag){
            redisUtil.setEX(phone, code, TIME_OUT, TimeUnit.MINUTES);
            return new Result().setData(code);
        }
        return new Result().setMessage("发送失败,网络繁忙");

    }

    /**
     * 登陆使用的临时对象
     */

    public static class HeadToken{
        private String headToken;

        public String getHeadToken() {
            return headToken;
        }

        public void setHeadToken(String headToken) {
            this.headToken = headToken;
        }
    }
    public static class LoginUser extends HeadToken{


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
     *
     * */
    public static  class  RegisterUser extends HeadToken{
        private  String phone;

        private String password;

        private String  code;

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
     *修改密码专用类
     * */
    public static  class UpdateUser extends HeadToken{

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
