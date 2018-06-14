package com.jieweifu.common.utils;

import com.jieweifu.models.Result;
import com.jieweifu.models.WeatherData;
import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static Result getWeather(String city, String lng, String lat) {
//        String host = "http://www.pm25.in";
//        String path = "/api/querys/pm2_5.json";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE "); //工具类定义好了这个参数必不可少，这里随便设置一个，只是不让为空
//        Map<String, String> query = new HashMap<>();
        String method = "GET";
//        query.put("city", city);
//        query.put("token", APPKEY);
//
//        String qualityResult = "";
//        try {
//            HttpResponse response = HttpUtil.doGet(host, path, method, headers, query);
//            String result = EntityUtils.toString(response.getEntity());
//            //因为这个返回的结果是 这样的 [{"":""},..]  带有大括号，所以这个String没法直接转换成json类型，这里把他的大括号去掉
//            qualityResult = result.substring(1, result.length() - 1);
//
//        } catch (Exception e) {
//            return new Result().setError("请求异常，请稍后重试");
//        }

//        JSONObject jsonObjectList = new JSONObject();
//        jsonObjectList.put("AirQuality", JSONObject.fromObject(qualityResult));

        String weatherResult = "";
        Map<String, String> locationQuery = new HashMap<>();
        locationQuery.put("lon", lng);
        locationQuery.put("lat", lat);
        Map<String, String> codeQuery = new HashMap<>();
        try {
            HttpResponse locationResponse = HttpUtil.doGet("http://d7.weather.com.cn", "/fishing/api/v1/location", method, headers, locationQuery);
            String location = EntityUtils.toString(locationResponse.getEntity());
            JSONObject locationObj = JSONObject.fromObject(location);
            String locationCode = locationObj.getJSONObject("result").getString("id");
            codeQuery.put("id", locationCode);
            HttpResponse response = HttpUtil.doGet("http://d7.weather.com.cn", "/fishing/api/v1/actual", method, headers, codeQuery);
            weatherResult = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("请求异常，请稍后重试");
        }

//        jsonObjectList.put("Weather", weatherResult);
        return new Result().setData(JSONObject.fromObject(weatherResult));
    }
}