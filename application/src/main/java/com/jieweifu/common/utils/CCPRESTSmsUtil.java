package com.jieweifu.common.utils;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by 陶Lyn
 * on 2018/3/14.
 */

/**
 * 发送短信验证码工具类
 * */
public class CCPRESTSmsUtil {

    protected static final Logger logger = LoggerFactory.getLogger(CCPRESTSmsUtil.class);

    public static boolean sendSMSByYunXunTong(String phone, String templateId, String[] content) {

        CCPRestSmsSDK ccpRestSmsSDK = new CCPRestSmsSDK();

        ccpRestSmsSDK.init("app.cloopen.com", "8883");
        ccpRestSmsSDK.setAccount("8aaf070867c517d20167ce7e791305fb","c7445b7733dd464eb04b1b2609886bc9");
        ccpRestSmsSDK.setAppId("8aaf070867c517d20167ce7e79760602");

        HashMap<String,Object> result = ccpRestSmsSDK.sendTemplateSMS(phone, templateId, content);

        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();

            for (String key:keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
            return true;
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
            return false;
        }

    }


}
