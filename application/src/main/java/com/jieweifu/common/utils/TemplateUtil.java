package com.jieweifu.common.utils;

import net.sf.json.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * https请求工具集
 */
public class TemplateUtil {

    /**
     * http请求静态方法,支持post,get,put,delete
     * @param url 访问url
     * @param headMap 消息头
     * @param json body数据
     * @param method 请求类型
     * @return json
     */
    public static JSONObject restHttp(String url, Map<String, String> headMap, JSONObject json, HttpMethod method) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (headMap != null) {
            headMap.forEach(
                    httpHeaders::set
            );
        }
        HttpEntity<JSONObject> entity = new HttpEntity<>(json, httpHeaders);
        HttpEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
        String result = response.getBody();
        JSONObject jsonObject = null;
        if (result == null || (result.startsWith("[") && result.endsWith("]"))) {
            String object = "{'date':" + result + "}";
            jsonObject = JSONObject.fromObject(object.replaceAll("null", "''"));
        } else {
            jsonObject = JSONObject.fromObject(result.replaceAll("null", "''"));
        }
        return jsonObject;
    }

}
