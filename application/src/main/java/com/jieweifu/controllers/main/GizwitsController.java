package com.jieweifu.controllers.main;
import com.jieweifu.common.gizwits.GizwitsNoti;
import com.jieweifu.common.gizwits.Setting;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.gizwits.*;
import com.jieweifu.services.gizwits.GizwitsService;
import com.jieweifu.services.gizwits.impl.GizwitsServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by 陶Lyn
 * on 2018/5/4.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("main/gizwits")
public class GizwitsController {




   @Autowired
   private GizwitsService gizwitsService;


    @Autowired
    GizwitsNoti gizwitsNoti;


    //开启与机智云的连接
    @AdminAuthAnnotation(check = false)
    @GetMapping("/initGizwits")
    public  void initGizwits(){
        gizwitsNoti.init();
    }



    //获得大project
    @AdminAuthAnnotation(check = false)
    @PostMapping("/getGizwitsProject")
    public Result getGizwitsProject(@RequestBody GizwitsMessage gizwitsMessage){
        String mac =gizwitsMessage.getMac();

        GizwitsProject gizwitsProject=gizwitsService.getProject(mac);
        return new Result().setData(gizwitsProject);
    }


    @AdminAuthAnnotation(check = false)
    @PostMapping("/getAbc")
    public Result getGizwitsProject1(@RequestBody GizwitsMessage gizwitsMessage){
        String mac =gizwitsMessage.getMac();
        String id =gizwitsMessage.getDeviceId();
        String type= gizwitsService.getDeviceNameById(id,mac);
        return new Result().setData(type);
    }



    //获得设备的状态的状态
    @AdminAuthAnnotation(check = false)
    @PostMapping("/getDeviceStatus")
    public Result getDeviceStatus(@RequestBody GizwitsMessage gizwitsMessage){
        String mac =gizwitsMessage.getMac();
        String deviceId=gizwitsMessage.getDeviceId();
        String deviceName =gizwitsMessage.getType();

        if(deviceName.equals("light")){
            GizwitsStatusLight gizwitsStatusLight=gizwitsService.getLightStatus(mac,deviceId);
            return new Result().setData(gizwitsStatusLight);
        }else if(deviceName.equals("curtain")){
            GizwitsStatusCurtain gizwitsStatusCurtain=gizwitsService.getCurtainStatus(mac,deviceId);
            return new Result().setData(gizwitsStatusCurtain);
        }else if(deviceName.equals("hvac")) {
            GizwitsStatusHvac gizwitsStatusHvac=gizwitsService.getHavcStatus(mac,deviceId);
            return new Result().setData(gizwitsStatusHvac);
        }
        return new Result().setError("找不到设备");
    }











    /**
     * 接收机智云信息的临时类
     */

    public static class GizwitsMessage {

        private String type;
        private String mac;
        private String deviceId;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }
}
