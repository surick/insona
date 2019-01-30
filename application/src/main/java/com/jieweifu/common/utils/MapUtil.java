package com.jieweifu.common.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Jin
 * @date 2019/1/30
 */
public class MapUtil {
    protected static final Logger logger = LoggerFactory.getLogger(MapUtil.class);

    private static final String BAIDU_MAP = "http://api.map.baidu.com/geocoder?location=:lat,:lng&output=json&key=:key";

    public static String getCityByGeo(String lat, String lng, String key) throws IOException {
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url(BAIDU_MAP.replace(":lat", lat)
                            .replace(":lng", lng)
                            .replace(":key", key))
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("maputil error --> {}", e);
            return "获取失败";
        }
    }

    public static void main(String... args) throws Exception{
        String res = getCityByGeo("31.2990", "120.5853", "01FNnTFgvnMRi6TfsOehb94Z");
        System.out.println(res);
    }
}
