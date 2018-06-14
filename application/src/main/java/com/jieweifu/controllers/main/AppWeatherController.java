package com.jieweifu.controllers.main;

import com.jieweifu.common.utils.WeatherUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.WeatherData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 陶Lyn
 * on 2018/3/15.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("/function")
public class AppWeatherController {


//City 输入的是中文
  @GetMapping("/getWeather")
    public Result getWeather(String City,String lng, String lat){
       return  WeatherUtil.getWeather(City,lng,lat);
    }
}
