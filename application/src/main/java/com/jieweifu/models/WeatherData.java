package com.jieweifu.models;

/**
 * Created by 陶Lyn
 * on 2018/3/19.
 */
public class WeatherData {
    private String tem1;
    private String tem2;
    private String temNow;
    private String humidity;
    private String time;
    private String windPower;
    private String windDir;
    private String windState;
    private String stateDetailed;
    private String cityname;

    public WeatherData() {

    }

    public WeatherData(String tem1, String tem2, String temNow, String humidity, String time,
                       String windPower, String windDir, String windState, String stateDetailed, String cityname) {
        this.tem1 = tem1;
        this.tem2 = tem2;
        this.temNow = temNow;
        this.humidity = humidity;
        this.time = time;
        this.windPower = windPower;
        this.windDir = windDir;
        this.windState = windState;
        this.stateDetailed = stateDetailed;
        this.cityname = cityname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWindPower() {
        return windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindState() {
        return windState;
    }

    public void setWindState(String windState) {
        this.windState = windState;
    }

    public String getStateDetailed() {
        return stateDetailed;
    }

    public void setStateDetailed(String stateDetailed) {
        this.stateDetailed = stateDetailed;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getTem1() {
        return tem1;
    }

    public void setTem1(String tem1) {
        this.tem1 = tem1;
    }

    public String getTem2() {
        return tem2;
    }

    public void setTem2(String tem2) {
        this.tem2 = tem2;
    }

    public String getTemNow() {
        return temNow;
    }

    public void setTemNow(String temNow) {
        this.temNow = temNow;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
