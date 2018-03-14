package com.jieweifu.common.utils;

/**
 * Created by 陶Lyn
 * on 2018/3/14.
 */

import java.util.Random;

/**
 * 随机6位数字生成短信验证码
 * */

public class SMSUtil {
    //随机生成6位数字
    public static  String getVerificationCode(){
        String smsCode="";
        for(int i=0;i<6;i++){
            char c=(char) (randomInt(0,10)+'0');
            smsCode+=String.valueOf(c);
        }
        return smsCode;
    }

    public static  int randomInt(int from,int to){
        Random r =new Random();
        return from+r.nextInt(to-from);
    }


}
