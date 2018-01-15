package com.jieweifu.controllers.GizWits;

import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.common.utils.TemplateUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.models.insona.Url;
import com.jieweifu.models.regex.Regex;
import com.jieweifu.services.insona.TerminalUserService;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("unused")
@RestController("GizWitsUsers")
@RequestMapping("giz/users")
public class UsersController {
    /* 微信的appid */
    private static String WeChatAppid = "1";
    /* QQ的appi */
    private static String appid = "1";
    /* 状态验证码 */
    private static String state = "1";

    private RedisUtil redisUtil;
    private Url url;
    private TerminalUserService terminalUserService;

    @Autowired
    public UsersController(RedisUtil redisUtil, Url url, TerminalUserService terminalUserService) {
        this.redisUtil = redisUtil;
        this.url = url;
        this.terminalUserService = terminalUserService;
    }


    /**
     * 获取用户参数
     * GET
     * http://api.gizwits.com/app/users
     * 连接请求参数:
     * X-Gizwits-Application-Id	string
     * X-Gizwits-User-token	string
     * 返回值     类型    描述
     * username  	string	用户名
     * phone    	string	手机号码
     * email    	string	注册邮箱
     * name     	string	姓名
     * gender   	string	性别，M：男, F：女, N：未知
     * birthday  	string	生日，日期格式：YYYY-MM-DD or MM-DD
     * address   	string	地址
     * lang 	    string	语言版本en, zh-cn
     * remar    	string	备注
     * uid      	string	用户唯一id
     * is_anonymous	boolean	是否匿名用户
     */
    @GetMapping("getUser")
    public Result getUser() {
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getGetUser(), getHeader(), null, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 创建用户
     * POST
     * http://api.gizwits.com/app/users
     * 参数
     * 必填:X-Gizwits-Application-Id
     * 类型   必填 参数类型  描述
     * phone_id	string	否	body	匿名标识,匿名注册的请求参数
     * username	string	否	body	用户名,用户名密码注册的请求参数
     * password	string	否	body	密码,用户名密码注册、手机注册、邮箱注册的请求参数
     * email	string	否	body	邮件地址,邮箱注册的请求参数
     * phone	string	否	body	手机号码,手机号码，手机注册的请求参数
     * code	    string	否	body	验证码,短信验证码，手机注册的请求参数
     * lang	    string	否	body	语言:en，zh-cn
     * src	    string	否	body	平台类型:qq,sina,baidu,wechat,twitter,facebook,google, amazon
     * uid	    string	否	body	第三方登录平台返回的uid
     * token	string	否	body	第三方登录平台返回的token
     */
    @PostMapping("postUser")
    public Result postUser(@RequestBody JSONObject object) {
        String appid = String.valueOf(object.get("appid"));
        if (appid == null || appid.equals("")) {
            return new Result().setError("appid不能为空");
        }
        redisUtil.set("GWappid", appid);
        redisUtil.delete("GWUserToken");
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getPostUser(), getHeader(), object, HttpMethod.POST);
        if (!StringUtils.isBlank(String.valueOf(jsonObject.get("uid")))
                && !StringUtils.isBlank(String.valueOf(jsonObject.get("username")))) {
            InsonaUser insonaUser = new InsonaUser();
            insonaUser.setUsername(String.valueOf(jsonObject.get("username")));
            insonaUser.setUid(String.valueOf(jsonObject.get("uid")));
            terminalUserService.save(insonaUser);
        }
        return new Result().setData(jsonObject);
    }

    /**
     * 修改用户信息
     * PUT
     * http://api.gizwits.com/app/users
     * 必填参数                  类型    参数类型 描述
     * X-Gizwits-Application-Id	string	header	appid
     * X-Gizwits-User-token 	string	header	用户token
     * 参数	     类型	必填	参数类型	描述
     * username	string	否	body	用户名，用于匿名转普通用户
     * password	string	否	body	密码，用于匿名转普通用户
     * phone	string	否	body	手机号码，用于匿名转手机注册用户
     * code  	string	否	body	验证码，用于匿名转手机注册用户
     * old_pwd	string	否	body	旧密码，用于修改密码
     * new_pwd	string	否	body	新密码，用于修改密码
     * email	string	否	body	注册邮箱，用于匿名转邮箱注册用户
     * name	    string	否	body	姓名
     * gender	string	否	body	性别，M：男, F：女, N：未知
     * birthday	string	否	body	生日，日期格式：YYYY-MM-DD or MM-DD
     * address	string	否	body	地址
     * lang 	string	否	body	语言版本en, zh-cn
     * remark	string	否	body	备注
     *
     * @return 返回值
     * 参数       	类型	    描述
     * uid	        string	用户唯一id
     * token	    string	用户token
     * expire_at	integer	token过期时间（时间戳）
     */
    @PutMapping("putUser")
    public Result putUser(@RequestBody JSONObject object) {
        JSONObject result =
                TemplateUtil.restHttp(url.getPutUser(), getHeader(), object, HttpMethod.PUT);
        InsonaUser insonaUser = terminalUserService.get(getStr(object,"username"));
        insonaUser.setUsername(getStr(object,"username"));
        insonaUser.setPhone(getStr(object,"phone"));
        insonaUser.setEmail(getStr(object,"email"));
        insonaUser.setName(getStr(object,"name"));
        insonaUser.setGender(getStr(object,"gender"));
        insonaUser.setBirthday(getStr(object,"birthday"));
        insonaUser.setAddress(getStr(object,"address"));
        insonaUser.setLang(getStr(object,"lang"));
        insonaUser.setRemar(getStr(object,"remark"));
        terminalUserService.update(insonaUser);
        return new Result().setData(result);
    }
    String getStr(JSONObject object, String key) {
        String result;
        if (object.get(key) == null){
            result = "";
            return result;
        }else {
            try {
                result = String.valueOf(object.get(key));
            } catch (Exception e){
                result = "";
            }
        }
        return result;
    }

    /**
     * 用户登录
     * POST
     * http://api.gizwits.com/app/login
     * 请求参数                   类型	必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token 	string	是	header	用户token
     * username             	string	是	body	用户名，可以是用户的 username/email/phone
     * password             	string	是	body	密码
     * lang                 	string	否	body	语言版本
     *
     * @return 响应参数
     * <p>
     * 参数	        类型	    描述
     * uid	        string	用户唯一id
     * token	    string	用户token
     * expire_at	integer	token过期时间（时间戳）
     */
    @PostMapping("login/{id}")
    public Result login(@RequestBody JSONObject json,
                        @PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        Map<String, String> map1 = new HashMap<>();
        map1.put("X-Gizwits-Application-Id", id);
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getPostLogin(), map1, json, HttpMethod.POST);
        String userToken = jsonObject.get("token").toString();
        Integer timeValue = Integer.valueOf(jsonObject.get("expire_at").toString()) - 20;
        redisUtil.setEX("GWUserToken", userToken, timeValue, TimeUnit.SECONDS);
        redisUtil.set("GWappid", id);
        return new Result().setData(jsonObject);
    }

    /**
     * 获取Token
     * POST
     * http://api.gizwits.com/app/request_token
     * 该接口获取的token参数主要用于调用获取短信验证码和获取图片验证码接口时作为请求参数使用。
     * <p>
     * 请求参数	                    类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	    string	是	header	appid
     * X-Gizwits-Application-Auth	string	是	header	MD5_32位加密(appid + appsecret)
     * <p>
     * 响应参数	    类型  	描述
     * uid	        string	用户唯一id
     * expire_at	integer	token过期时间（时间戳）
     */
    @PostMapping("requestToken")
    public Result requestToken(@RequestBody Map<String, String> map) {

        String id = map.get("id");
        String appsecret = map.get("appsecret");
        if ((id.matches(Regex.NOTNULL_REX)) || (appsecret.matches(Regex.NOTNULL_REX))) {
            return new Result().setError("参数不合法");
        }
        String auth = DigestUtils.md5Hex(id + appsecret);
        Map<String, String> map1 = new HashMap<>();
        map1.put("X-Gizwits-Application-Id", id);
        map1.put("X-Gizwits-Application-Auth", auth);
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getPostToken(), map1, null, HttpMethod.POST);
        String GWToken = jsonObject.get("token").toString();
        Integer timeValue = Integer.valueOf(String.valueOf(jsonObject.get("expired_at"))) - 20;
        redisUtil.setEX("GWToken", GWToken, timeValue, TimeUnit.MILLISECONDS);
        return new Result().setData(jsonObject);
    }

    /**
     * 重置密码
     * POST
     * http://api.gizwits.com/app/reset_password
     * 只有设置了 email 或者 phone 的用户才可以重置密码。
     * 通过 email 重置密码，只需传入 email ，通过邮件发送重置密码链接
     * 通过 phone 重置密码，传入 phone, new_pwd, code (通过调用获取短信验证码接口获取)
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * email	                string	否	body	注册邮箱
     * phone	                string	否	body	注册手机号码
     * new_pwd	                string	否	body	新密码
     * code	                    string	否	body	验证码
     * <p>
     * 响应参数
     * 无
     */
    @PostMapping("resetPassword")
    public Result resetPassword(@RequestBody JSONObject json) {
        if (!String.valueOf(json.get("email")).equals("")
                || !String.valueOf(json.get("phone")).equals("")) {
            if (!String.valueOf(json.get("code")).matches(Regex.NOTNULL_REX)) {
                JSONObject jsonObject =
                        TemplateUtil.restHttp(url.getPostPassword(), getHeader(), json, HttpMethod.POST);
                return new Result().setData(jsonObject);
            } else {
                return new Result().setError("验证码不能为空");
            }
        }
        return new Result().setError("请完善重置密码必要的信息");
    }

    /**
     * 验证码的获取和校验
     * POST
     * http://api.gizwits.com/app/sms_code
     * 短信验证码的主要用途有：
     * 手机号用户注册
     * 手机号用户重置密码
     * 其他您认为需要短信验证码的敏感操作
     * 接口能实现获取和校验验证码：
     * 获取短信验证码只需要传入 phone
     * 验证短信验证码需要传入 phone 和 code
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token,通过获取APP Token接口取得
     * phone	                string	是	body	手机号码
     * code	                    string	是	body	验证码
     * <p>
     * 响应参数
     * 无
     */
    @PostMapping("smsCode")
    public Result smsCode(@RequestBody JSONObject json) {

        if (String.valueOf(json.get("phone")).matches(Regex.PHONE_REX)) {
            JSONObject jsonObject =
                    TemplateUtil.restHttp(url.getSmsCode(), getHeader(), json, HttpMethod.POST);
            return new Result().setData(jsonObject);
        }
        return new Result().setMessage("手机号或验证码有误");
    }

    /**
     * 获取图片验证码
     * GET
     * http://api.gizwits.com/app/verify/codes
     * 返回的 captcha_url 就是图片验证码的 URL，将图片显示给用户
     * <p>
     * 请求参数	                 类型  必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token,通过获取APP Token接口取得
     * <p>
     * 响应参数	    类型	    描述
     * captcha_url	string	图片验证码URL地址
     * captcha_id	string	图片验证码id
     */
    @GetMapping("getCodes/{id}")
    public Result getCodes(@PathVariable String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getGetCodes(), getHeader(), null, HttpMethod.GET);
        String captcha_id = jsonObject.get("captcha_id").toString();
        redisUtil.set("captcha_id", captcha_id);
        return new Result().setData(jsonObject);
    }

    /**
     * 校验图片验证码并发送验证码
     * POST
     * http://api.gizwits.com/app/verify/codes
     * 对获取图片验证码接口的图片验证码进行校验，校验通过将发送短信验证码
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token,通过获取APP Token接口取得
     * captcha_id	            string	是	body	图片验证码id
     * captcha_code	            string	是	body	图片验证码的值
     * phone	                string	是	body	手机号码
     * <p>
     * 响应参数
     * 无
     */
    @PostMapping("postCodes")
    public Result postCodes(@RequestBody Map<String, String> map) {
        String captcha_id = String.valueOf(redisUtil.get("captcha_id"));
        map.put("captcha_id", captcha_id);
        JSONObject json = JSONObject.fromObject(map);
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getPostCodes(), getHeader(), json, HttpMethod.POST);
        return new Result().setData(jsonObject);

    }

    /**
     * 校验短信验证码
     * GET
     * https://api.gizwits.com/app/verify/codes
     * <p>
     * 请求参数	                类型	   必填	参数类型	描述
     * X-Gizwits-Application-Id	string	是	header	appid
     * X-Gizwits-User-token	    string	是	header	用户token,通过获取APP Token接口取得
     * phone	                string	是	body	手机号码
     * sms_code	                string	是	body	验证码
     * <p>
     * 响应参数
     * 无
     */
    @GetMapping("putCodes")
    public Result putCodes(@RequestBody JSONObject json) {
        JSONObject jsonObject =
                TemplateUtil.restHttp(url.getPutCodes(), getHeader(), json, HttpMethod.GET);
        return new Result().setData(jsonObject);
    }

    /**
     * 获取qq登录的code
     * 请求地址：
     * PC网站：https://graph.qq.com/oauth2.0/authorize
     * 请求方法：
     * GET
     */
    @GetMapping("getAuthCode")
    public void getAuthCode() {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("response_type", "code");
            map.put("redirect_uri", URLEncoder.encode("url/getAccessToken", "utf-8"));
            map.put("client_id", appid);
            map.put("state", state);
            JSONObject jsonObject = JSONObject.fromObject(map);
            TemplateUtil.restHttp("https://graph.qq.com/oauth2.0/authorize", null, jsonObject, HttpMethod.GET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取QQ登录的accessToken
     * 通过Authorization Code获取Access Token
     * 请求地址：
     * PC网站：https://graph.qq.com/oauth2.0/token
     * 请求方法：
     * GET
     */
    @GetMapping("getAccessToken?code={code}&state={state}")
    public void getAccessToken(@PathVariable("code") String code, @PathVariable("state") String state) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "authorization_code");
        map.put("client_id", appid);
        /*QQ的appkey*/
        String appkey = "1";
        map.put("client_secret", appkey);
        map.put("code", code);
        map.put("redirect_uri", "getAccessToken");
        JSONObject object = JSONObject.fromObject(map);
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://graph.qq.com/oauth2.0/token", null, object, HttpMethod.GET);
        String accessToken = jsonObject.get("access_token").toString();
        redisUtil.set("accessToken", accessToken);
        getOpenId();
        redisUtil.set("src", "wechat");
    }

    /**
     * 获取QQ登录的openId
     * 请求地址
     * PC网站：https://graph.qq.com/oauth2.0/me
     * 请求方法
     * GET
     */
    private void getOpenId() {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", String.valueOf(redisUtil.get("accessToken")));
        JSONObject object = JSONObject.fromObject(map);
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://graph.qq.com/oauth2.0/me", null, object, HttpMethod.GET);
        String openId = jsonObject.get("openid").toString();
        redisUtil.set("openId", openId);
    }

    /**
     * 第三方登录
     * 第三方登录用户创建，通过authData内的 src、 uid 和 token 创建用户。
     * 目前支持腾讯QQ、微信
     */
    @PostMapping("postUsersByOther/{id}")
    public Result postUsersByOther(@PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数不能为空");
        }
        Map<String, String> authDate = new HashMap<>();
        Map<String, Map<String, String>> map = new HashMap<>();
        String src = String.valueOf(redisUtil.get("src"));
        String accessToken = String.valueOf(redisUtil.get("accessToken"));
        String openId = String.valueOf(redisUtil.get("openId"));
        authDate.put("src", src);
        authDate.put("uid", openId);
        authDate.put("token", accessToken);
        map.put("authData", authDate);
        JSONObject json = JSONObject.fromObject(map);
        Map<String, String> map1 = new HashMap<>();
        map1.put("X-Gizwits-Application-Id", id);
        JSONObject jsonObject =
                TemplateUtil.restHttp("http://api.gizwits.com/app/users", map1, json, HttpMethod.POST);
        String GWToken = jsonObject.get("token").toString();
        Integer timeValue = Integer.valueOf(jsonObject.get("expire_at").toString()) - 20000;
        redisUtil.setEX("GWToken", GWToken, timeValue, TimeUnit.MILLISECONDS);
        return new Result().setData(jsonObject);
    }

    /**
     * 获取微信code
     * 请求url:https://open.weixin.qq.com/connect/qrconnect
     * 请求方式:get
     * 用户允许授权后，将会重定向到redirect_uri的网址上，并且带上code和state参数
     * 若用户禁止授权，则重定向后不会带上code参数，仅会带上state参数
     */
    @GetMapping("getWeChatCode")
    public void getWeChatCode() {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("appid", WeChatAppid);
            map.put("redirect_uri", URLEncoder.encode("url/getWeChatToken", "utf-8"));
            map.put("response_type", "code");
            map.put("scope", "snsapi_login");
            map.put("state", state);
            JSONObject object = JSONObject.fromObject(map);
            TemplateUtil.restHttp("https://open.weixin.qq.com/connect/qrconnect", null, object, HttpMethod.GET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取微信Token和openId
     * 请求url:https://api.weixin.qq.com/sns/oauth2/access_token
     * 请求方式:get
     * 返回值:
     * access_token	接口调用凭证
     * expires_in	access_token接口调用凭证超时时间，单位（秒）
     * refresh_token	用户刷新access_token
     * openid	授权用户唯一标识
     * scope	用户授权的作用域，使用逗号（,）分隔
     * unionid	当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
     */
    @GetMapping("getWeChatToken?code={code}&state={state}")
    public void getWeChatToken(@PathVariable("code") String code, @PathVariable("state") String state) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", WeChatAppid);
        /*微信的secret*/
        String weChatSecret = "1";
        map.put("secret", weChatSecret);
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        JSONObject object = JSONObject.fromObject(map);
        JSONObject jsonObject =
                TemplateUtil.restHttp("https://api.weixin.qq.com/sns/oauth2/access_token", null, object, HttpMethod.GET);
        String accessToken = jsonObject.get("access_token").toString();
        String openId = jsonObject.get("openid").toString();
        redisUtil.set("accessToken", accessToken);
        redisUtil.set("openId", openId);
        redisUtil.set("src", "wechat");
    }

    /**
     * 微信取消授权
     */
    @GetMapping("getWeChatToken?state={state}")
    public Result getFailedAuth(@PathVariable("state") String state) {
        return new Result().setMessage("微信授权失败");
    }

    private Map<String, String> getHeader() {
        String token = String.valueOf(redisUtil.get("GWUserToken"));
        Map<String, String> map = new HashMap<>();
        String appid = String.valueOf(redisUtil.get("GWappid"));
        map.put("X-Gizwits-Application-Id", appid);
        map.put("X-Gizwits-User-token", token);
        return map;
    }
}
