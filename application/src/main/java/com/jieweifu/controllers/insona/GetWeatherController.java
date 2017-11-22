package com.jieweifu.controllers.insona;

import com.jieweifu.common.utils.HttpUtil;
import com.jieweifu.models.Result;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("InsonaWeather")
@RequestMapping("insona/weather")
public class GetWeatherController {

    private static String appcode = "2209d626f654471b8718c0288a6d72ad";

    @GetMapping("getWeather")
    public Result getWeather(@Param("city") String city,
                             @Param("citycode") String citycode,
                             @Param("cityid") String cityid,
                             @Param("ip") String ip,
                             @Param("location") String location) {
        String host = "http://jisutqybmf.market.alicloudapi.com";
        String path = "/weather/query";
        String method = "GET";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("city", city);
        querys.put("citycode", citycode);
        querys.put("cityid", cityid);
        querys.put("ip", ip);
        querys.put("location", location);
        JSONObject jsonObject = null;
        JSONObject object = null;
        try {
            HttpResponse response = HttpUtil.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            jsonObject = JSONObject.fromObject(result);
        } catch (Exception e) {
            return new Result().setError("请求异常,请稍后重试");
        }
        return new Result().setData(jsonObject);
    }

}
