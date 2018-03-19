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

    public static Result getAirQuality(String City) {
        String chinese_py=getPinYin(City);
        String host = "http://www.pm25.in";
        String path = "/api/querys/pm2_5.json";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE ");//工具类定义好了这个参数必不可少，这里随便设置一个，只是不让为空
        Map<String, String> querys = new HashMap<>();
        String method="GET";
        querys.put("city", chinese_py);
        querys.put("token", APPKEY);
        //String content ="city="+city+"&token="+APPKEY;
        String newResult = "";
        try {
            HttpResponse response= HttpUtil.doGet(host,path,method,headers,querys);
            String result= EntityUtils.toString(response.getEntity());
            //因为这个返回的结果是 这样的 [{"":""},..]  带有大括号，所以这个String没法直接转换成json类型，这里把他的大括号去掉
            newResult = result.substring(1,result.length()-1);
            System.out.println("===="+newResult);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("请求异常，请稍后重试");
        }

        JSONObject jsonObjectList=new JSONObject();
        jsonObjectList.put("AirQuality",JSONObject.fromObject(newResult));

        List<WeatherData> jsonlist = new ArrayList();
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            //这个url返回的是一个xml格式的数据  所以  需要 通过jdom 把他转换成json
            doc = builder.build("http://flash.weather.com.cn/wmaps/xml/" + chinese_py + ".xml");
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element root = doc.getRootElement();
        List<Element> list = root.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Element city = list.get(i);
            String tem1 = city.getAttributeValue("tem1");
            String tem2 = city.getAttributeValue("tem2");
            String temNow = city.getAttributeValue("temNow");
            String humidity = city.getAttributeValue("humidity");
            String time = city.getAttributeValue("time");
            String windPower = city.getAttributeValue("windPower");
            String windDir = city.getAttributeValue("windDir");
            String windState = city.getAttributeValue("windState");
            String stateDetailed = city.getAttributeValue("stateDetailed");
            String cityname = city.getAttributeValue("cityname");
            WeatherData wd = new WeatherData(tem1, tem2, temNow, humidity, time, windPower, windDir, windState, stateDetailed, cityname);
            jsonlist.add(wd);
        }
        jsonObjectList.put("WeatherCondition",jsonlist);
        return new Result().setData(jsonObjectList);
    }

    public static String getPinYin(String chinese) {
        char[] t1 = null;
        t1 = chinese.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);

        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {

                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组?

                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4?
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串t4?
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        return t4;
    }

}