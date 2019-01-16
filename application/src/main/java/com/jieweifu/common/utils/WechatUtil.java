package com.jieweifu.common.utils;

import com.alibaba.fastjson.JSON;
import com.jieweifu.models.wechat.OAuth2AccessToken;
import com.jieweifu.models.wechat.WeixinUserInfo;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin
 * @date 2019/1/16
 */
public class WechatUtil {
    protected static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    private static final String APP_ID = "wx89a7462acaf6dccc";
    private static final String APP_SECRECT = "ad35d7a7ff3adeac39994e86d42e3c82";
    /**
     * 网页授权获取code
     */
    public static final String OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
    /**
     * code换取网页授权access_token
     */
    public static final String OAUTH_ASSCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     * 如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息了。
     */
    public static final String SNSAPI_USERINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    public static OAuth2AccessToken getAccessToken(String code) {
        String url = String.format(OAUTH_ASSCESSTOKEN, APP_ID, APP_SECRECT, code);

        OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject res = JSONObject.fromObject(response.body().string());
            if (res.has("errcode")) {
                logger.info("获取openid失败");
                return null;
            }
            OAuth2AccessToken oAuth2AccessToken = JSON.parseObject(res.toString(), OAuth2AccessToken.class);
            return oAuth2AccessToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static WeixinUserInfo getWeixinUserInfo(String accessToken, String openId) {
        String url = String.format(SNSAPI_USERINFO, accessToken, openId);

        OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject res = JSONObject.fromObject(response.body().string());
            if (res.has("errcode")) {
                logger.info("获取微信用户信息失败");
                return null;
            }
            WeixinUserInfo weixinUserInfo = JSON.parseObject(res.toString(), WeixinUserInfo.class);
            return weixinUserInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String... args) {
        OAuth2AccessToken oAuth2AccessToken = getAccessToken("011ajF582sZvSJ0FCs782O11682ajF5Q");
        WeixinUserInfo weixinUserInfo = getWeixinUserInfo(oAuth2AccessToken.getAccess_token(), oAuth2AccessToken.getOpenid());
        System.out.println(weixinUserInfo);
    }
}
