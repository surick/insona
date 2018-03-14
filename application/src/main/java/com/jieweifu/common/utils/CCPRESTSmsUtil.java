package com.jieweifu.common.utils;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

import java.util.HashMap;

/**
 * Created by 陶Lyn
 * on 2018/3/14.
 */
public class CCPRESTSmsUtil {
    public static boolean  sendSMSByYunXunTong (String phone,String templateId,String[]content){
        CCPRestSmsSDK ccpRestSmsSDK=new CCPRestSmsSDK();
        ccpRestSmsSDK.init("app.cloopen.com", "8883");
        ccpRestSmsSDK.setAccount("8aaf070862181ad50162227e60ec0676","a3c6f3ec387c44b48586f6e4cfc98c18");
        ccpRestSmsSDK.setAppId("8aaf070862181ad50162227e615c067d");
        //String []a={"1234","2"};
        HashMap<String,Object> data=ccpRestSmsSDK.sendTemplateSMS(phone,templateId,content);
       if(data!=null){
           if("000000".equals(data.get("statusCode"))){
                return true;
           }
       }
       return false;
    }

   /* public static void main(String[] args) {
        String []a={"1234","2"};
        if( sendSMSByYunXunTong("18136098944","1",a)){
            System.out.println("====true");
        }
        System.out.println("====false");

    }*/

}
