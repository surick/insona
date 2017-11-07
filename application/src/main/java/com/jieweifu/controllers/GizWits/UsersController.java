package com.jieweifu.controllers.GizWits;


import com.jieweifu.common.utils.HttpUtil;
import com.jieweifu.common.utils.Md5Util;
import com.jieweifu.models.Result;

import com.jieweifu.models.regex.Regex;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")
@RestController("GizWitsUsers")
@RequestMapping("giz/users")
public class UsersController {

    public static String Token = null;


    /**
     * 得到用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("getAppUsers/{id}/{appsecret}")
    public Result getAppUsers(@PathVariable("id") String id, @PathVariable("appsecret") String appsecret) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        Map<String, String> map = new HashMap<>();
        JSONObject jsonObject = HttpUtil.sendGet("http://api.gizwits.com/app/users", id, getToken(id, appsecret), null);
        return new Result().setData(jsonObject);
    }


    /**
     * 创建用户
     *
     * @param map
     * @param id
     * @return
     */
    @PostMapping("postAppUsers/{id}")
    public Result postAppUsers(@RequestBody Map<String, String> map, @PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数不能为空");
        }
        JSONObject json = JSONObject.fromObject(map);
        JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/users", id, null, null, json);
        return new Result().setData(jsonObject);
    }

    /**
     * 修改用户信息
     *
     * @param map
     * @param id
     * @return
     */
    @PutMapping("putAppUsers/{id}/{appsecret}")
    public Result putAppUsers(@RequestBody Map<String, String> map,
                              @PathVariable("id") String id,
                              @PathVariable("appsecret") String appsecret) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        JSONObject json = JSONObject.fromObject(map);
        JSONObject result = HttpUtil.sendPut("http://api.gizwits.com/app/users", id, getToken(id, appsecret), null, json);
        return new Result().setData(result);
    }

    /**
     * 用户登录
     *
     * @param map
     * @param id
     * @return
     */
    @PostMapping("postAppLogin/{id}/{appsecret}")
    public Result postAppLogin(@RequestBody Map<String, String> map,
                               @PathVariable("id") String id,
                               @PathVariable("appsecret") String appsecret) {
        if (id.matches(Regex.NOTNULL_REX)
                || map.get("username").matches(Regex.NOTNULL_REX) || map.get("password").matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        JSONObject json = JSONObject.fromObject(map);
        JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/login", id, getToken(id, appsecret), null, json);
        return new Result().setData(jsonObject);
    }

    /**
     * Token调用方法
     *
     * @param id
     * @param appsecret
     * @return
     */
    public String getToken(String id, String appsecret) {
        if ((id.matches(Regex.NOTNULL_REX)) || (appsecret.matches(Regex.NOTNULL_REX))) {
            return null;
        }
        String auth = Md5Util.encrypt32(id + appsecret);
        JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/request_token", id, null, auth, null);
        String token = jsonObject.get("token").toString();
        return token;
    }

    /**
     * 获取Token
     *
     * @param map
     * @return
     */
    @PostMapping("postAppRequestToken")
    public Result postAppRequestToken(@RequestBody Map<String, String> map) {

        String id = map.get("id");
        String appsecret = map.get("appsecret");
        if ((id.matches(Regex.NOTNULL_REX)) || (appsecret.matches(Regex.NOTNULL_REX))) {
            return new Result().setError("参数不合法");
        }
        String auth = Md5Util.encrypt32(id + appsecret);
        JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/request_token", id, null, auth, null);
        Token = jsonObject.get("token").toString();
        return new Result().setData(jsonObject);
    }

    /**
     * 重置密码
     *
     * @param map
     * @param id
     * @return
     */
    @PostMapping("postAppResetPassword/{id}")
    public Result postAppResetPassword(@RequestBody Map<String, String> map, @PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        if (map.get("email").matches(Regex.EMAIL_REX)
                || map.get("phone").matches(Regex.PHONE_REX)) {
            if (map.get("new_pwd").matches(Regex.PASSWORD_REX) &&
                    map.get("code").matches(Regex.NOTNULL_REX)) {
                JSONObject json = JSONObject.fromObject(map);
                JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/reset_password", id, null, null, json);
                return new Result().setData(jsonObject);
            }
        }
        return new Result().setError("请完善重置密码必要的信息");
    }

    /**
     * 验证码的获取和校验
     *
     * @param map
     * @return
     */
    @PostMapping("postAppSmsCode/{id}/{appsecret}")
    public Result postAppSmsCode(@RequestBody Map<String, String> map,
                                 @PathVariable("id") String id,
                                 @PathVariable("appsecret") String appsecret) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        if (map.get("phone").matches(Regex.PHONE_REX) && !map.get("code").matches(Regex.NOTNULL_REX)) {
            JSONObject json = JSONObject.fromObject(map);
            JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/sms_code", id, getToken(id, appsecret), null, json);
            return new Result().setData(jsonObject);
        }
        return new Result().setMessage("手机号或验证码有误");
    }

    /**
     * 获取图片验证码
     *
     * @param id
     * @param appsecret
     * @return
     */
    @GetMapping("getAppVerifyCodes/{id}/{appsecret}")
    public Result getAppVerifyCodes(@PathVariable String id,
                                    @PathVariable("appsecret") String appsecret) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        JSONObject jsonObject = HttpUtil.sendGet("http://api.gizwits.com/app/verify/codes", id, getToken(id, appsecret), null);
        return new Result().setData(jsonObject);
    }

    /**
     * 校验图片验证码并发送验证码
     *
     * @param map
     * @param id
     * @param appsecret
     * @return
     */
    @PostMapping("postAppVerifyCodes/{id}/{appsecret}")
    public Result postAppVerifyCodes(@RequestBody Map<String, String> map,
                                     @PathVariable("id") String id,
                                     @PathVariable("appsecret") String appsecret) {
        if (id.matches(Regex.NOTNULL_REX) ||
                map.get("captcha_id").matches(Regex.NOTNULL_REX) ||
                map.get("captcha_code").matches(Regex.NOTNULL_REX) ||
                map.get("phone").matches(Regex.NOTNULL_REX)) {
            return new Result().setError("请完善信息");
        }
        JSONObject json = JSONObject.fromObject(map);
        HttpUtil.sendPost("http://api.gizwits.com/app/verify/codes", id, getToken(id, appsecret), null, json);
        return new Result().setMessage("短信已发送");

    }

    /**
     * 校验短信验证码
     *
     * @param id
     * @param phone
     * @param code
     * @param appsecret
     * @return
     */
    @GetMapping("putAppVerifyCodes/{id}/{phone}/{code}/{appsecret}")
    public Result putAppVerifyCodes(@PathVariable("id") String id,
                                    @PathVariable("phone") String phone,
                                    @PathVariable("code") String code,
                                    @PathVariable("appsecret") String appsecret) {
        if (!id.matches(Regex.NOTNULL_REX) &&
                !phone.matches(Regex.NOTNULL_REX) && !code.matches(Regex.NOTNULL_REX)) {
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("sms_code", code);
            JSONObject jsonObject = HttpUtil.sendGet("https://api.gizwits.com/app/verify/codes", id, getToken(id, appsecret), map);
            return new Result().setData(jsonObject);
        }
        return new Result().setError("校验失败");
    }
}
