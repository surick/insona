package com.jieweifu.common.utils;

import com.jieweifu.models.Result;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陶Lyn
 * on 2018/3/15.
 */
public class WeatherUtil {
    /**
     * 天气查询接口
     * url   ： http://www.pm25.in/api/querys/pm2_5.json?city=珠海&token=xxxxxx 或者http://www.pm25.in/api/querys/pm2_5.json?city=zhuhai&token=xxxxxx
     *参数  city 城市  拼音与中文都可以
     * token 必须携带的参数  这里暂时只使用测试用的  APPKEY
     * 返回  见 http://www.pm25.in/api_doc
     * */

    private static final String APPKEY = "5j1znBVAsnSf5xQyNQyq";

    public static Result getAirQuality(String city) {

        String host = "http://www.pm25.in";
        String path = "/api/querys/pm2_5.json";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE ");//工具类定义好了这个参数必不可少，这里随便设置一个，只是不让为空
        Map<String, String> querys = new HashMap<>();
        String method="GET";
        querys.put("city", city);
        querys.put("token", APPKEY);
        //String content ="city="+city+"&token="+APPKEY;
        JSONObject jsonObject=null;
        try {
            HttpResponse response= HttpUtil.doGet(host,path,method,headers,querys);
            String result= EntityUtils.toString(response.getEntity());
            //因为这个返回的结果是 这样的 [{"":""},..]  带有大括号，所以这个String没法直接转换成json类型，这里把他的大括号去掉
           String newResult=result.substring(1,result.length()-1);
            System.out.println("===="+newResult);
            jsonObject=JSONObject.fromObject(newResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("请求异常，请稍后重试");
        }
        return new Result().setData(jsonObject);
    }


}
