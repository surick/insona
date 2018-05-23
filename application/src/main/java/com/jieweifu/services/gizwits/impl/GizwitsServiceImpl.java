package com.jieweifu.services.gizwits.impl;

import com.jieweifu.common.dbservice.DB;

import com.jieweifu.common.gizwits.FindJsonUtil;
import com.jieweifu.common.gizwits.SocketQ;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.models.gizwits.*;
import com.jieweifu.services.gizwits.GizwitsService;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.*;


/**
 * Created by 陶Lyn
 * on 2018/4/28.
 */
@Service
public class GizwitsServiceImpl implements GizwitsService {

    @Autowired
    private DB db;
    @Autowired
    private RedisUtil redisUtil;


    Base64.Decoder decoder = Base64.getDecoder();


    @Override
    public void analysisJsonAndConserve(JSONObject json) {
        GizwitsControl gizwitsControl = (GizwitsControl) JSONObject.toBean(json, GizwitsControl.class);
        String mac = gizwitsControl.getMac();
        String did = gizwitsControl.getDid();
        float stemp = gizwitsControl.getCreated_at();
        BigDecimal bigDecimal = new BigDecimal(stemp);
        String newTime = bigDecimal.toString();
        String time = FindJsonUtil.timeStamp2Date(newTime);
        System.out.println("===时间戳转换的时间==》" + time);
        String base = "";
        if (!gizwitsControl.getData().get("raw").equals("") && !gizwitsControl.getData().get("raw").equals(null)) {
            base = gizwitsControl.getData().get("raw");
        }
        String baseCompile = "";
        try {
            baseCompile = new String(decoder.decode(base), "UTF-8");
        } catch (Exception e) {
            System.out.println("baocuo");
        }
        FindJsonUtil fju = new FindJsonUtil();
        List<String> list = fju.format(baseCompile);
        for (String aa : list) {
            JSONObject jsonSF = JSONObject.fromObject(aa);
            if (jsonSF.containsKey("responses")) {
                Map<String, Class<Responses>> map = new HashMap<String, Class<Responses>>();
                map.put("responses", Responses.class);
                GizwitsDevice gizwitsDevice = (GizwitsDevice) JSONObject.toBean(jsonSF, GizwitsDevice.class, map);
                Responses responses = gizwitsDevice.getResponses().get(0);
                if (responses.getData() != null && responses.getData().getItem() != null && responses.getData().getItem().getType().equals("project")) {
                    System.out.println("是最大的project");
                    GizwitsProject gizwitsProject = new GizwitsProject();
                    gizwitsProject.setDid(did);
                    gizwitsProject.setMac(mac);
                    gizwitsProject.setCreateTime(time);
                    gizwitsProject.setJsonObject(aa);
                    db.insert()
                            .save(gizwitsProject)
                            .execute();

                    redisUtil.set(mac,aa);
                } else {
                    System.out.println("是操作");
                    GizwitsOperation gizwitsOperation = new GizwitsOperation();
                    gizwitsOperation.setDid(did);
                    gizwitsOperation.setMac(mac);
                    gizwitsOperation.setCreateTime(time);
                    gizwitsOperation.setJsonObject(aa);
                    db.insert()
                            .save(gizwitsOperation)
                            .execute();
                }
            } else {
                if (jsonSF.containsKey("status")) {
                   String id= jsonSF.getString("id");
                    String deviceName =this.getDeviceNameById(id,mac);

                    //如果是状态，直接通过socketio推送到与之连接的前端
                    SocketIODeviceEntity socketIODeviceEntity=new SocketIODeviceEntity(mac,aa);
                    if(SocketQ.client!=null){
                        SocketQ.client.sendEvent(socketIODeviceEntity.getMac(),socketIODeviceEntity);
                    }
                    if(deviceName!=null){
                        if(deviceName.equals("light")){
                            GizwitsStatusLight gizwitsStatusLight = new GizwitsStatusLight();
                            gizwitsStatusLight.setDid(did);
                            gizwitsStatusLight.setMac(mac);
                            gizwitsStatusLight.setCreateTime(time);
                            gizwitsStatusLight.setJsonObject(aa);
                            db.insert()
                                    .save(gizwitsStatusLight)
                                    .execute();
                        }else if(deviceName.equals("curtain")){
                            GizwitsStatusCurtain gIzwitsStatusCurtain = new GizwitsStatusCurtain();
                            gIzwitsStatusCurtain.setDid(did);
                            gIzwitsStatusCurtain.setMac(mac);
                            gIzwitsStatusCurtain.setCreateTime(time);
                            gIzwitsStatusCurtain.setJsonObject(aa);
                            db.insert()
                                    .save(gIzwitsStatusCurtain)
                                    .execute();
                        }else if(deviceName.equals("hvac")){
                            GizwitsStatusHvac gizwitsStatusHvac = new GizwitsStatusHvac();
                            gizwitsStatusHvac.setDid(did);
                            gizwitsStatusHvac.setMac(mac);
                            gizwitsStatusHvac.setCreateTime(time);
                            gizwitsStatusHvac.setJsonObject(aa);
                            db.insert()
                                    .save(gizwitsStatusHvac)
                                    .execute();
                        }else if(deviceName.equals("media")){

                        }else {

                        }
                    }else{
                        System.out.println("设备不存在");
                    }

                } else {
                    System.out.println("未知类型");
                }
            }
        }
    }
    @Override
    public GizwitsProject getProject(String mac) {
        return db.select()
                .from(GizwitsProject.class)
                .where("mac=?", mac)
                .orderBy("createTime DESC")
                .limit(0, 1)
                .queryForEntity(GizwitsProject.class);
    }

    @Override
    public GizwitsStatusLight getLightStatus(String mac, String deviceId) {
        return db.select()
                .from(GizwitsStatusLight.class)
                .where("mac=?", mac)
                .where("deviceId=?", deviceId)
                .orderBy("createTime DESC")
                .limit(0, 1)
                .queryForEntity(GizwitsStatusLight.class);
    }

    @Override
    public GizwitsStatusCurtain getCurtainStatus(String mac, String deviceId) {

        return db.select()
                .from(GizwitsStatusCurtain.class)
                .where("mac=?", mac)
                .where("deviceId=?", deviceId)
                .orderBy("createTime DESC")
                .limit(0, 1)
                .queryForEntity(GizwitsStatusCurtain.class);
    }


    @Override
    public GizwitsStatusHvac getHavcStatus(String mac, String deviceId) {
        return db.select()
                .from(GizwitsStatusHvac.class)
                .where("mac=?", mac)
                .where("deviceId=?", deviceId)
                .orderBy("createTime DESC")
                .limit(0, 1)
                .queryForEntity(GizwitsStatusHvac.class);
    }


    @Override
    public List<JSONObject> handleProjectJson(String jsonString) {
        JSONObject jsonObject1 = JSONObject.fromObject(jsonString);
        List<JSONObject> list = new ArrayList<>();
        if (jsonObject1.containsKey("responses")) {
            System.out.println("包含responses的json 排除状态  ");
            JSONArray jsonArray1 = JSONArray.fromObject(jsonObject1.get("responses"));
            JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
            JSONObject jsonObject3 = JSONObject.fromObject(jsonObject2.get("data"));
            if (jsonObject3.containsKey("item")) {
                JSONObject jsonObject4 = JSONObject.fromObject(jsonObject3.get("item"));
                JSONArray jsonArray2 = JSONArray.fromObject(jsonObject4.get("subitems"));
                for (int i = 0; i < jsonArray2.size(); i++) {
                    list.add(jsonArray2.getJSONObject(i));
                }
            }
        }
        Iterator<JSONObject> iterator = list.iterator();
        while (iterator.hasNext()) {
            JSONObject jsonObject = iterator.next();
            if (!jsonObject.get("type").equals("device")) {
                iterator.remove();
            }
        }
        return list;
    }
    @Override
    public String getDeviceNameById(String id, String mac) {
        String type = null;
        String jsonObject=null;
        Object  object= redisUtil.get(mac);
        if (object!=null){
            jsonObject=object.toString();
        }else {
            GizwitsProject gizwitsProject = this.getProject(mac);
            System.out.println("==找大project时"+mac);
            jsonObject= gizwitsProject.getJsonObject();
        }
        if(jsonObject==null){
            return null;
        }

        List<JSONObject>  deviceObject= this.handleProjectJson(jsonObject);
        for(JSONObject device : deviceObject){
            JSONArray jsonArray= JSONArray.fromObject( device.get("subitems"));
            System.out.println("===找的array");
            for(int i=0;i<jsonArray.size();i++){
                if(jsonArray.getJSONObject(i).getString("id").equals(id)){
                    System.out.println("===找的id并且相同");
                    return jsonArray.getJSONObject(i).getString("type");
                }else{
                   return  null;
                }
            }
        }
        return null;
    }




}
