package com.jieweifu.controllers.GizWits;


import com.jieweifu.common.business.BaseContextHandler;
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

    @GetMapping("getAppUsers/{id}")
    public Result getAppUsers(@PathVariable("id") String id) {

        if (id.matches(Regex.NOTNULL_REX) || id.equals("0")) {
            return new Result().setError("参数不能为空");
        }
        Map<String, String> map = new HashMap<>();
        String token = getToken();
        JSONObject jsonObject = HttpUtil.sendGet("http://api.gizwits.com/app/users",id,token,null);
        return new Result().setData(jsonObject);
    }

    @PostMapping("postAppUsers")
    public Result postAppUsers(@RequestBody Map<String, String> map) {
        if (map.get("X-Gizwits-Application-Id").matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数不能为空");
        }
        String result = HttpUtil.sendPost("http://api.gizwits.com/app/users", map);
        return new Result().setData(result);
    }

    @PutMapping("putAppUsers")
    public Result putAppUsers(@RequestBody Map<String, String> map) {
        if (map.get("X-Gizwits-Application-Id").matches(Regex.NOTNULL_REX)||
                map.get("X-Gizwits-User-token").matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数不能为空");
        }
        String result = HttpUtil.sendPut("http://api.gizwits.com/app/users", map);
        String s = result.replace("\\", "");
        return new Result().setMessage(s);
    }


    @PostMapping("postAppLogin")
    public Result postAppLogin(@RequestBody Map<String, String> map) {
        if (map.get("X-Gizwits-Application-Id").matches(Regex.NOTNULL_REX) &&
                map.get("X-Gizwits-User-token").matches(Regex.NOTNULL_REX)&&
                map.get("username").matches(Regex.NOTNULL_REX) && map.get("password").matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数不合法"+map.get("username").matches(Regex.NOTNULL_REX)+map.get("password").matches(Regex.NOTNULL_REX));

        }
        String result = HttpUtil.sendPost("http://api.gizwits.com/app/login", map);
        return new Result().setMessage(result);
    }

    public String getToken(){

        String id = (String) BaseContextHandler.get("appId");
        String appsecret = (String) BaseContextHandler.get("appsecret");
        return postAppRequestToken(id,appsecret).getData().toString();
    }

    /**
     * 获取Token
     * @param id
     * @param appsecret
     * @return
     */
    @PostMapping("postAppRequestToken")
    public Result postAppRequestToken(@RequestBody String id, @RequestBody String appsecret) {
        if ((id.matches(Regex.NOTNULL_REX)) || (appsecret.matches(Regex.NOTNULL_REX))) {
            return new Result().setError("参数不合法");
        }
        String auth = Md5Util.encrypt32(id + appsecret);
        Map<String, String> map = new HashMap<>();
        String result = HttpUtil.sendPost("http://api.gizwits.com/app/request_token",id,null,auth,null);
        return new Result().setData(result);
    }

    @PostMapping("postAppResetPassword")
    public Result postAppResetPassword(@RequestBody Map<String, String> map) {
        if (!map.get("X-Gizwits-Application-Id").matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数不合法");
        }
        if (map.get("email").matches(Regex.EMAIL_REX)
                || map.get("phone").matches(Regex.PHONE_REX)) {
            if (map.get("new_pwd").matches(Regex.PASSWORD_REX) &&
                    map.get("code") != null && map.get("code") != "") {
                String result = HttpUtil.sendPost("http://api.gizwits.com/app/reset_password", map);
                return new Result().setMessage("重置密码短信/邮件发送成功,请及时查收");
            }
        }
        return new Result().setError("请完善重置密码必要的信息");
    }

    /**
     * @param map X-Gizwits-Application-Id   appsecret   phone  code
     * @return
     */
    @PostMapping("postAppSmsCode")
    public Result postAppSmsCode(@RequestBody Map<String, String> map) {
        if (!map.get("X-Gizwits-Application-Id").matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数不合法");
        }
        map.put("X-Gizwits-User-token", postAppRequestToken(map.get("X-Gizwits-Application-Id"), map.get("appsecret")).getData().toString());
        if (map.get("phone").matches(Regex.PHONE_REX) && map.get("code") != null && map.get("code") != "") {
            String result = null;
            result = HttpUtil.sendPost("http://api.gizwits.com/app/sms_code", map);
            return new Result().setMessage(result == null ? "发送短信成功" : "发送短信失败");
        }
        return new Result().setMessage("手机号或验证码有误");
    }

    @GetMapping("getAppVerifyCodes")
    public Result getAppVerifyCodes(@PathVariable String id,@PathVariable String appsecret) {
        if (id.matches(Regex.NOTNULL_REX) || appsecret.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数不合法");
        }
        String token = postAppRequestToken(id, appsecret).getData().toString();
        Map<String,String> map = new HashMap<>();
        map.put("X-Gizwits-Application-Id",id);
        map.put("X-Gizwits-User-token",token);
        String result = HttpUtil.sendGet("http://api.gizwits.com/app/verify/codes", map);
        return new Result().setData(result);
    }


    @PostMapping("postAppVerifyCodes")
    public Result postAppVerifyCodes(@RequestBody Map<String,String> map) {
        if(map.get("X-Gizwits-Application-Id").matches(Regex.NOTNULL_REX)&&
                map.get("X-Gizwits-User-token").matches(Regex.NOTNULL_REX)&&
                map.get("captcha_id").matches(Regex.NOTNULL_REX)&&
                map.get("phone").matches(Regex.NOTNULL_REX)){
            HttpUtil.sendPost("http://api.gizwits.com/app/verify/codes",map);
            return new Result().setMessage("短信已发送");
        }
        return new Result().setError("请完善信息");
    }

    @GetMapping("putAppVerifyCodes")
    public Result putAppVerifyCodes(@PathVariable String id,@PathVariable String appsecret,
                                  @PathVariable String phone,@PathVariable String code) {
        if(id.matches(Regex.NOTNULL_REX)&&appsecret.matches(Regex.NOTNULL_REX)&&
                phone.matches(Regex.NOTNULL_REX)&&code.matches(Regex.NOTNULL_REX)){
            Map<String,String> map = new HashMap<>();
            String token = postAppRequestToken(id, appsecret).getData().toString();
            map.put("X-Gizwits-Application-Id",id);
            map.put("X-Gizwits-User-token",token);
            map.put("phone",phone);
            map.put("sms_code",code);
            HttpUtil.sendGet("https://api.gizwits.com/app/verify/codes",map);
            return new Result().setMessage("校验成功");
        }
        return new Result().setError("校验失败");
    }
}
