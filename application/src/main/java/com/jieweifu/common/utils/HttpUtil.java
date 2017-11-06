package com.jieweifu.common.utils; /**
 *
 **************************************************************************************
 *
 *  Project:        ZXQ
 *
 *  Copyright ©     2014-2017 Banma Technologies Co.,Ltd
 *                  All rights reserved.
 *
 *  This software is supplied only under the terms of a license agreement,
 *  nondisclosure agreement or other written agreement with Banma Technologies
 *  Co.,Ltd. Use, redistribution or other disclosure of any parts of this
 *  software is prohibited except in accordance with the terms of such written
 *  agreement with Banma Technologies Co.,Ltd. This software is confidential
 *  and proprietary information of Banma Technologies Co.,Ltd.
 *
 **************************************************************************************
 *
 *  Class Name: com.test.MyTest.java
 *
 *  General Description:
 *
 *  Revision History:
 *                           Modification
 *   Author                Date(MM/DD/YYYY)   JiraID           Description of Changes
 *   ---------------------   ------------    ----------     -----------------------------
 *   LZY                   2017年8月29日
 *
 ***************************************************************************************
 *
 */



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 *
 */

public class HttpUtil {

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    /**
     * 发送HttpGet请求
     * @param url
     * @return
     */
    public static JSONObject sendGet(String url,String appId,String token,Map<String,String> map) {

        StringBuilder params = new StringBuilder();
        map.forEach(
                (s, s2) ->
                    params.append(s+":"+s2+"&&")

        );
        HttpGet httpget = null;
        httpget = new HttpGet(url+"?"+ params);
        httpget.setHeader("X-Gizwits-Application-Id",appId);
        if(token!=null){
            httpget.setHeader("X-Gizwits-User-token",token);
        }
        System.out.println(url+"?"+ params);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        JSONObject result = null;
        try {
            HttpEntity entity1 = response.getEntity();
            if (entity1 != null) {
                result = JSONObject.fromObject(entity1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送参数的HttpPost请求
     * @param url
     * @return
     */
    public static JSONObject sendPost(String url,String appId,String token,String auth,Map<String, String> map) {

        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("X-Gizwits-Application-Id",appId);
        if(token!=null){
            httppost.setHeader("X-Gizwits-User-token",token);
        }
        if(auth!=null){
            httppost.setHeader("X-Gizwits-Application-Auth",auth);
        }
        if(map!=null){
            List<NameValuePair> formparams = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            httppost.setEntity(entity);
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity1 = response.getEntity();
        JSONObject result = null;
        try {
            result = JSONObject.fromObject(entity1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送HttpPut请求，参数为map
     * @param url
     * @param map
     * @return
     */
    public static JSONObject sendPut(String url,String appId,String token,String auth, Map<String, String> map) {
        List<NameValuePair> formparams = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("X-Gizwits-Application-Id",appId);
        if(token!=null){
            httpPut.setHeader("X-Gizwits-User-token",token);
        }
        if(auth!=null){
            httpPut.setHeader("X-Gizwits-Application-Auth",auth);
        }
        httpPut.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity1 = response.getEntity();
        JSONObject result = null;
        try {
            result = JSONObject.fromObject(entity1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }




}
  
