package com.jieweifu.controllers.GizWits;

import com.jieweifu.common.utils.HttpUtil;
import com.jieweifu.common.utils.Md5Util;
import com.jieweifu.models.Result;
import com.jieweifu.models.regex.Regex;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")
@RestController("GizWitsUsers")
@RequestMapping("giz/users")
public class UsersController {
    /*由appid和appsecret生成的token*/
    private static String Token;
    /*用户登录的token*/
    private static String userToken;
    /**
     * 微信的appid
     */
    private static String WeChatAppid = "1";
    /**
     * 微信的secret
     */
    private static String WeChatSecret = "1";
    /**
     * QQ的appid
     */
    private static String appid = "1";
    /**
     * QQ的appkey
     */
    private static String appkey = "1";
    /*状态验证码*/
    private static String state = "1";
    /*accesstoken用于注册*/
    private static String accessToken;
    /*uid用于注册*/
    private static String openId;
    /*第三方识别*/
    private static String src;
    /*图片验证码id*/
    private static String captcha_id;


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
     *
     * @return
     */
    @GetMapping("getAppUsers/{id}")
    public Result getAppUsers(@PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        JSONObject jsonObject = HttpUtil.sendGet("http://api.gizwits.com/app/users", id, userToken, null, null);
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
        JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/users", id, null, null, null, json);
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
     * remar	string	否	body	备注
     *
     * @return 返回值
     * 参数       	类型	    描述
     * uid	        string	用户唯一id
     * token	    string	用户token
     * expire_at	integer	token过期时间（时间戳）
     */
    @PutMapping("putAppUsers/{id}")
    public Result putAppUsers(@RequestBody Map<String, String> map,
                              @PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        JSONObject json = JSONObject.fromObject(map);
        JSONObject result = HttpUtil.sendPut("http://api.gizwits.com/app/users", id, userToken, null, null, json);
        return new Result().setData(result);
    }

    /**
     * 用户登录
     * POST
     * http://api.gizwits.com/app/login
     * 请求参数                   	类型	   必填	参数类型	描述
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
    @PostMapping("postAppLogin/{id}")
    public Result postAppLogin(@RequestBody Map<String, String> map,
                               @PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX)
                || map.get("username").matches(Regex.NOTNULL_REX) || map.get("password").matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        if (map.get("username").matches(Regex.NOTNULL_REX) &&
                map.get("password").matches(Regex.NOTNULL_REX) &&
                !map.get("phone_id").matches(Regex.NOTNULL_REX)) {
            JSONObject json = JSONObject.fromObject(map);
            JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/login", id, null, null, null, json);
        }
        JSONObject json = JSONObject.fromObject(map);
        JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/login", id, null, null, null, json);
        String userToken = jsonObject.get("token").toString();
        System.out.println(userToken);
        UsersController.userToken = userToken;
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
        JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/request_token", id, null, null, auth, null);
        Token = jsonObject.get("token").toString();
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
        if (map.get("email") != null
                || map.get("phone") != null) {
            if (!map.get("code").matches(Regex.NOTNULL_REX)) {
                JSONObject json = JSONObject.fromObject(map);
                JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/reset_password", id, null, null, null, json);
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
     *
     * @param map
     * @return
     */
    @PostMapping("postAppSmsCode/{id}")
    public Result postAppSmsCode(@RequestBody Map<String, String> map,
                                 @PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        if (map.get("phone").matches(Regex.PHONE_REX)) {
            JSONObject json = JSONObject.fromObject(map);
            JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/sms_code", id, null, Token, null, json);
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
     *
     * @param id
     * @return
     */
    @GetMapping("getAppVerifyCodes/{id}")
    public Result getAppVerifyCodes(@PathVariable String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数错误");
        }
        System.out.println(Token);
        JSONObject jsonObject = HttpUtil.sendGet("http://api.gizwits.com/app/verify/codes", id, null, Token, null);
        captcha_id = jsonObject.get("captcha_id").toString();
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
     *
     * @param map
     * @param id
     * @return
     */
    @PostMapping("postAppVerifyCodes/{id}")
    public Result postAppVerifyCodes(@RequestBody Map<String, String> map,
                                     @PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX) &&
                map.get("phone").matches(Regex.NOTNULL_REX)) {
            return new Result().setError("请完善信息");
        }
        map.put("captcha_id", captcha_id);
        JSONObject json = JSONObject.fromObject(map);
        JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/verify/codes", id, null, Token, null, json);
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
     *
     * @param id
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("putAppVerifyCodes/{id}/{phone}/{code}")
    public Result putAppVerifyCodes(@PathVariable("id") String id,
                                    @PathVariable("phone") String phone,
                                    @PathVariable("code") String code) {
        if (!id.matches(Regex.NOTNULL_REX) &&
                !phone.matches(Regex.NOTNULL_REX) && !code.matches(Regex.NOTNULL_REX)) {
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("sms_code", code);
            JSONObject jsonObject = HttpUtil.sendGet("https://api.gizwits.com/app/verify/codes", id, null, Token, map);
            return new Result().setData(jsonObject);
        }
        return new Result().setError("校验失败");
    }

    /**
     * 获取qq登录的code
     * 请求地址：
     * PC网站：https://graph.qq.com/oauth2.0/authorize
     * 请求方法：
     * GET
     */
    @GetMapping("getAuthorizationCode")
    public void getAuthorizationCode() {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("response_type", "code");
            map.put("redirect_uri", URLEncoder.encode("url/getAccessToken", "utf-8"));
            map.put("client_id", appid);
            map.put("state", state);
            HttpUtil.getSSL("https://graph.qq.com/oauth2.0/authorize", map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 获取QQ登录的accessToken
     * 通过Authorization Code获取Access Token
     * 请求地址：
     * PC网站：https://graph.qq.com/oauth2.0/token
     * 请求方法：
     * GET
     *
     * @param code
     * @param state
     * @return
     */
    @GetMapping("getAccessToken?code={code}&state={state}")
    public void getAccessToken(@PathVariable("code") String code, @PathVariable("state") String state) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "authorization_code");
        map.put("client_id", appid);
        map.put("client_secret", appkey);
        map.put("code", code);
        map.put("redirect_uri", "getAccessToken");
        JSONObject jsonObject = HttpUtil.getSSL("https://graph.qq.com/oauth2.0/token", map);
        accessToken = jsonObject.get("access_token").toString();
        getOpenId();
        src = "qq";
        return;
    }

    /**
     * 获取QQ登录的openId
     * 请求地址
     * PC网站：https://graph.qq.com/oauth2.0/me
     * 请求方法
     * GET
     */
    public void getOpenId() {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        JSONObject jsonObject = HttpUtil.getSSL("https://graph.qq.com/oauth2.0/me", map);
        openId = jsonObject.get("openid").toString();
        return;
    }

    /**
     * 第三方登录
     * 第三方登录用户创建，通过authData内的 src、 uid 和 token 创建用户。
     * 目前支持腾讯QQ、微信
     *
     * @param id
     * @return
     */
    @PostMapping("postAppUsersByOther/{id}")
    public Result postAppUsersByQQ(@PathVariable("id") String id) {
        if (id.matches(Regex.NOTNULL_REX)) {
            return new Result().setError("参数不能为空");
        }
        Map<String, String> authDate = new HashMap<>();
        Map<String, Map<String, String>> map = new HashMap<>();

        authDate.put("src", src);
        authDate.put("uid", openId);
        authDate.put("token", accessToken);
        map.put("authData", authDate);
        JSONObject json = JSONObject.fromObject(map);
        JSONObject jsonObject = HttpUtil.sendPost("http://api.gizwits.com/app/users", id, null, null, null, json);
        Token = jsonObject.get("token").toString();
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
            HttpUtil.getSSL("https://open.weixin.qq.com/connect/qrconnect", map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return;
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
     *
     * @param code
     * @param state
     */
    @GetMapping("getWeChatToken?code={code}&state={state}")
    public void getWeChatToken(@PathVariable("code") String code, @PathVariable("state") String state) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", WeChatAppid);
        map.put("secret", WeChatSecret);
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        JSONObject jsonObject = HttpUtil.getSSL("https://api.weixin.qq.com/sns/oauth2/access_token", map);
        accessToken = jsonObject.get("access_token").toString();
        openId = jsonObject.get("openid").toString();
        src = "wechat";
        return;
    }

    /**
     * 微信取消授权
     *
     * @param state
     * @return
     */
    @GetMapping("getWeChatToken?state={state}")
    public Result getFailedAuth(@PathVariable("state") String state) {
        return new Result().setMessage("微信授权失败");
    }

}
