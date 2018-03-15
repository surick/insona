package com.jieweifu.controllers.main;

import com.jieweifu.common.utils.WeatherUtil;
import com.jieweifu.models.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 陶Lyn
 * on 2018/3/15.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("function")
public class FunctionController {

    @GetMapping("getWeather")
    public Result getWeather(String city) {
        return WeatherUtil.getAirQuality(city);
    }
}
