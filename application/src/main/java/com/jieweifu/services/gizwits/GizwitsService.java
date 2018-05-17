package com.jieweifu.services.gizwits;


import com.jieweifu.models.gizwits.*;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by 陶Lyn
 * on 2018/4/28.
 */
public interface GizwitsService {


    //解析机智云推送过来的信息并把区分保存进数据库
    void analysisJsonAndConserve(JSONObject json);

    //得到mac 与对应最近的project
    GizwitsProject getProject(String mac);

    //得到设备id  与mac地址对应最近的灯状态
    GizwitsStatusLight getLightStatus(String mac,String deviceId);

    //得到设备id  与mac地址对应最近的窗帘状态
    GizwitsStatusCurtain getCurtainStatus(String mac,String deviceId);

    //得到设备id  与mac地址对应最近的暖通（空调）状态
    GizwitsStatusHvac getHavcStatus(String mac,String deviceId);




    //操作project的json,得到包换device的所有json集合
    public List<JSONObject> handleProjectJson(String jsonString);

    //通过状态返回的id 与mac 的得到他对应的设备
    public String getDeviceNameById(String id,String mac);


    //通过前端传过来的 设备id  与mac地址找到


}
