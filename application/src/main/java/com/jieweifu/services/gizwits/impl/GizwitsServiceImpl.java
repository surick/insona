package com.jieweifu.services.gizwits.impl;

import com.jieweifu.common.dbservice.DB;

import com.jieweifu.common.gizwits.FindJsonUtil;
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
            System.out.println("==>" + aa.length());
            JSONObject jsonSF = JSONObject.fromObject(aa);
            if (jsonSF.containsKey("responses")) {
                System.out.println("包含responses的json 排除状态  ");
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
            jsonObject= gizwitsProject.getJsonObject();
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

    public static void main(String[] args) {
      /*  String base = "AAAAA940AACRBtzNAgIAABpSeyJpZCI6MCwicmVzcG9uc2VzIjpbeyJuYW1lIjoiZ2V0X2l0ZW0iLCJzZXF1ZW5jZSI6MCwicmV0Ijoib2siLCJkYXRhIjp7Iml0ZW0iOnsiaWQiOjAsInR5cGUiOiJwcm9qZWN0IiwibmFtZSI6IkV4aGliaXRpb24iLCJhdHRyaWJ1dGVzIjp7Im1hbnVmYWN0dXJlIjoiaW5Tb25hIENvLEx0ZCIsInByb3RvY29sX3ZlcnNpb24iOiIyLjAiLCJhcHBsaWNhdGlvbiI6IlNNQVJUX0hPTUUiLCJ0aW1lem9uZSI6Iis4OjAwIiwicHJvamVjdF92ZXJzaW9uIjoiMjAxODA1MDYiLCJsb2NhdGlvbiI6IlN1emhvdSIsImhvc3RfZGV2aWNlIjo1OH0sInN1Yml0ZW1zIjpbeyJpZCI6MSwidHlwZSI6ImNvbnRhaW5lciIsIm5hbWUiOiJFeGhpYml0aW9uIEhhbGwiLCJhdHRyaWJ1dGVzIjp7ImVuZHBvaW50cyI6Wzc1LDgxXSwiaHZhY3MiOltdLCJjdXJ0YWlucyI6W10sImxpZ2h0cyI6WzQxLDQ2LDUxLDU2LDYsOCwxNywxOSwyNSwyNywzMywzNV19LCJzdWJpdGVtcyI6W3siaWQiOjIsInR5cGUiOiJjb250YWluZXIiLCJuYW1lIjoiQmVkcm9vbSIsImF0dHJpYnV0ZXMiOnsiZW5kcG9pbnRzIjpbXSwiaHZhY3MiOltdLCJjdXJ0YWlucyI6WzYzLDY4XSwibGlnaHRzIjpbXX0sInN1Yml0ZW1zIjpbXX1dfSx7ImlkIjozLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkxpZ2h0IENvbnRyb2xsZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9rZXlwYWRfc3dpdGNoVjIiLCJtYW51ZiI6ImluU29uYSIsIm1vZGVsIjoiSU4tQzAxLU1GUC1TeCIsInNuIjoiMDAwZDZmMDAwZGU3ZDVmMSIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS40Iiwic3d2ZXJzaW9uIjoiMi4zLjMuMCJ9fSwic3ViaXRlbXMiOlt7ImlkIjo2LCJ0eXBlIjoibGlnaHQiLCJuYW1lIjoiU3dpdGNoIExpZ2h0IDEiLCJhdHRyaWJ1dGVzIjp7ImxpZ2h0X3R5cGUiOiJQRU5EQU5UIiwiY29sb3JhYmxlIjpmYWxzZSwiZGltbWVyYWJsZSI6ZmFsc2V9LCJzdWJpdGVtcyI6W119LHsiaWQiOjgsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJTd2l0Y2ggTGlnaHQgMiIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IkJFTFQiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjpmYWxzZX0sInN1Yml0ZW1zIjpbXX0seyJpZCI6MTEsInR5cGUiOiJrZXlwYWQiLCJuYW1lIjoia2V5cGFkIiwiYXR0cmlidXRlcyI6eyJzdXBwb3J0QWN0aW9uIjpbXSwiYnV0dG9uQXR0cmlidXRlIjpbXSwiY29sb3JhYmxlIjp0cnVlLCJwcmVzZXRfY29sb3JfbGlzdCI6W10sImxlZEFjdGlvbiI6W10sImJ1dHRvbkNvdW50Ijo0fSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjE0LCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkxpZ2h0IENvbnRyb2xsZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9rZXlwYWRfc3dpdGNoVjIiLCJtYW51ZiI6ImluU29uYSIsIm1vZGVsIjoiSU4tQzAxLU1GUC1TeCIsInNuIjoiMDAwZDZmMDAwZGU4OTkzMSIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS40Iiwic3d2ZXJzaW9uIjoiMi4zLjMuMCJ9fSwic3ViaXRlbXMiOlt7ImlkIjoxNywidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCAzIiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiUEVOREFOVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfSx7ImlkIjoxOSwidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA0IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiQkVMVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjIyLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkxpZ2h0IENvbnRyb2xsZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9rZXlwYWRfc3dpdGNoVjIiLCJtYW51ZiI6ImluU29uYSIsIm1vZGVsIjoiSU4tQzAxLU1GUC1TeCIsInNuIjoiMDAwZDZmMDAwZGU4ZjQ3MCIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS40Iiwic3d2ZXJzaW9uIjoiMi4zLjMuMCJ9fSwic3ViaXRlbXMiOlt7ImlkIjoyNSwidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA1IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiUEVOREFOVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfSx7ImlkIjoyNywidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA2IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiQkVMVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjMwLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkxpZ2h0IENvbnRyb2xsZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9rZXlwYWRfc3dpdGNoVjIiLCJtYW51ZiI6ImluU29uYSIsIm1vZGVsIjoiSU4tQzAxLU1GUC1TeCIsInNuIjoiMDAwZDZmMDAwZGU4ODliYyIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS40Iiwic3d2ZXJzaW9uIjoiMi4zLjMuMCJ9fSwic3ViaXRlbXMiOlt7ImlkIjozMywidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA3IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiUEVOREFOVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfSx7ImlkIjozNSwidHlwZSI6ImxpZ2h0IiwibmFtZSI6IlN3aXRjaCBMaWdodCA4IiwiYXR0cmlidXRlcyI6eyJsaWdodF90eXBlIjoiQkVMVCIsImNvbG9yYWJsZSI6ZmFsc2UsImRpbW1lcmFibGUiOmZhbHNlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjM4LCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkRpbW1lciBMaWdodCBDb250cm9sbGVyIiwiYXR0cmlidXRlcyI6eyJkcml2ZXIiOiIuL2RyaXZlci9pblNvbmFfa2V5cGFkX2RpbW1lclYyIiwibWFudWYiOiJpbnNvbmEiLCJtb2RlbCI6IklOLUMwMS1NRlAtRHgiLCJzbiI6IjAwMGQ2ZjAwMTBmZDIxOGUiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NDEsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJEaW1tZXIgTGlnaHQgMSIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IldBTEwiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjp0cnVlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjQzLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkRpbW1lciBMaWdodCBDb250cm9sbGVyIiwiYXR0cmlidXRlcyI6eyJkcml2ZXIiOiIuL2RyaXZlci9pblNvbmFfa2V5cGFkX2RpbW1lclYyIiwibWFudWYiOiJpbnNvbmEiLCJtb2RlbCI6IklOLUMwMS1NRlAtRHgiLCJzbiI6IjAwMGQ2ZjAwMTBmZDMxNjIiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NDYsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJEaW1tZXIgTGlnaHQgMiIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IldBTEwiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjp0cnVlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjQ4LCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkRpbW1lciBMaWdodCBDb250cm9sbGVyIiwiYXR0cmlidXRlcyI6eyJkcml2ZXIiOiIuL2RyaXZlci9pblNvbmFfa2V5cGFkX2RpbW1lclYyIiwibWFudWYiOiJpbnNvbmEiLCJtb2RlbCI6IklOLUMwMS1NRlAtRHgiLCJzbiI6IjAwMGQ2ZjAwMTBmZDMxZGMiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NTEsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJEaW1tZXIgTGlnaHQgMyIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IldBTEwiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjp0cnVlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjUzLCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkRpbW1lciBMaWdodCBDb250cm9sbGVyIiwiYXR0cmlidXRlcyI6eyJkcml2ZXIiOiIuL2RyaXZlci9pblNvbmFfa2V5cGFkX2RpbW1lclYyIiwibWFudWYiOiJpbnNvbmEiLCJtb2RlbCI6IklOLUMwMS1NRlAtRHgiLCJzbiI6IjAwMGQ2ZjAwMTBmZDUwMTAiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NTYsInR5cGUiOiJsaWdodCIsIm5hbWUiOiJEaW1tZXIgTGlnaHQgNCIsImF0dHJpYnV0ZXMiOnsibGlnaHRfdHlwZSI6IldBTEwiLCJjb2xvcmFibGUiOmZhbHNlLCJkaW1tZXJhYmxlIjp0cnVlfSwic3ViaXRlbXMiOltdfV19LHsiaWQiOjU4LCJ0eXBlIjoiZGV2aWNlIiwibmFtZSI6IkhvbWVDZW50ZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL2luU29uYV9oYyIsIm1hbnVmIjoiaW5Tb25hIiwibW9kZWwiOiJJTi1IQy0wMSIsInNuIjoiMDAwMDAwMDAwMDAwMDAwMCIsInZlcnNpb24iOnsiaHd2ZXJzaW9uIjoiMS4wLjAuMCIsInN3dmVyc2lvbiI6IjEuMC4wLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NTksInR5cGUiOiJ6YXAiLCJuYW1lIjoiemlnYmVlIGNvbnRyb2xlciBvZiBob21lY2VudGVyIiwiYXR0cmlidXRlcyI6e30sInN1Yml0ZW1zIjpbXX1dfSx7ImlkIjo2MiwidHlwZSI6ImRldmljZSIsIm5hbWUiOiJDdXJ0YWluIENvbnRyb2xsZXIgMSIsImF0dHJpYnV0ZXMiOnsiZHJpdmVyIjoiLi9kcml2ZXIvZGVtby9pblNvbmFfY3VydGFpbiIsIm1hbnVmIjoiaW5Tb25hIiwibW9kZWwiOiJJTi1DMDEtV0NNLURUODJFIiwic24iOiIwMDBkNmYwMDBkZDI4M2ZkIiwidmVyc2lvbiI6eyJod3ZlcnNpb24iOiIxLjQiLCJzd3ZlcnNpb24iOiIyLjMuMy4wIn19LCJzdWJpdGVtcyI6W3siaWQiOjYzLCJ0eXBlIjoiY3VydGFpbiIsIm5hbWUiOiJCZWRyb29tIERyYXBlIDEiLCJhdHRyaWJ1dGVzIjp7ImFuZ2xlYWJsZSI6ZmFsc2UsImN1cnRhaW50eXBlIjoiRFJBUEUiLCJwZXJjZW50YWJsZSI6dHJ1ZX0sInN1Yml0ZW1zIjpbXX1dfSx7ImlkIjo2NywidHlwZSI6ImRldmljZSIsIm5hbWUiOiJDdXJ0YWluIENvbnRyb2xsZXIgMiIsImF0dHJpYnV0ZXMiOnsiZHJpdmVyIjoiLi9kcml2ZXIvZGVtby9pblNvbmFfY3VydGFpbiIsIm1hbnVmIjoiaW5Tb25hIiwibW9kZWwiOiJJTi1DMDEtV0NNLURUODJFIiwic24iOiIwMDBkNmYwMDBkZDI5YWMwIiwidmVyc2lvbiI6eyJod3ZlcnNpb24iOiIxLjQiLCJzd3ZlcnNpb24iOiIyLjMuMy4wIn19LCJzdWJpdGVtcyI6W3siaWQiOjY4LCJ0eXBlIjoiY3VydGFpbiIsIm5hbWUiOiJCZWRyb29tIERyYXBlIDIiLCJhdHRyaWJ1dGVzIjp7ImFuZ2xlYWJsZSI6ZmFsc2UsImN1cnRhaW50eXBlIjoiRFJBUEUiLCJwZXJjZW50YWJsZSI6dHJ1ZX0sInN1Yml0ZW1zIjpbXX1dfSx7ImlkIjo3MiwidHlwZSI6ImRldmljZSIsIm5hbWUiOiJZb3V6aHVhbiBNdXNpYyBQbGF5ZXIiLCJhdHRyaWJ1dGVzIjp7ImRyaXZlciI6Ii4vZHJpdmVyL3lvdXpodWFuL1laLTMwMCIsIm1hbnVmIjoiWW91emh1YW4iLCJtb2RlbCI6IllaLTMwMCIsInNuIjoiMDAwMDAwMDAiLCJ2ZXJzaW9uIjp7InNvZnR3YXJlIjoiWVotMzAwIG4uMjAxNjA1MzAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NzMsInR5cGUiOiJtZWRpYSIsIm5hbWUiOiJZb3V6aHVhbiBNdXNpYyBQbGF5ZXIiLCJhdHRyaWJ1dGVzIjp7InBsYXllcnR5cGUiOiJMT0NBTF9QTEFZRVIiLCJlbmFibGVyZW1vdGUiOnRydWUsIm1lZGlhdHlwZSI6IkFVRElPIiwicmVtb3RlX2tleXMiOlsiVk9MVVAiLCJWT0xET1dOIiwiTVVURSIsIlBMQVkiLCJQQVVTRSIsIk5FWFQiLCJQUkVWIl19LCJzdWJpdGVtcyI6W119XX0seyJpZCI6NzgsInR5cGUiOiJkZXZpY2UiLCJuYW1lIjoiTWVkaWEgQ29udHJvbGxlciIsImF0dHJpYnV0ZXMiOnsiZHJpdmVyIjoiLi9kcml2ZXIvZGVtby9pblNvbmFfbWVkaWEiLCJtYW51ZiI6IkJldml4IiwibW9kZWwiOiJIaXRhY2gtQUM1MDAiLCJzbiI6IjAwZGYxMjMyZTA4YzIyNTAiLCJ2ZXJzaW9uIjp7Imh3dmVyc2lvbiI6IjEuNCIsInN3dmVyc2lvbiI6IjIuMy4zLjAifX0sInN1Yml0ZW1zIjpbeyJpZCI6NzksInR5cGUiOiJtZWRpYSIsIm5hbWUiOiJCZXZpeCBtZWRpYSIsImF0dHJpYnV0ZXMiOnsicGxheWVydHlwZSI6IkxPQ0FMX1BMQVlFUiIsImVuYWJsZXJlbW90ZSI6dHJ1ZSwibWVkaWF0eXBlIjoiVklERU8iLCJyZW1vdGVfa2V5cyI6WyJQV1IiLCJQV1JPTiIsIlBXUk9GRiIsIlNUQU5EQlkiLCJWT0xVUCIsIlZPTERPV04iLCJNVVRFIiwiUExBWSIsIlBBVVNFIiwiU1RPUCIsIlJFQyIsIk5FWFQiLCJQUkVWIiwiRkZXRCIsIkZSRVYiLCJTRldEIiwiVVAiLCJET1dOIiwiTEVGVCIsIlJJR0hUIiwiRU5URVIiLCJDTEVBUiIsIk5VTTAiLCJOVU0xIiwiTlVNMiIsIk5VTTMiLCJOVU00IiwiTlVNNSIsIk5VTTYiLCJOVU03IiwiTlVNOCIsIk5VTTkiLCJBU1RFUklTSyIsIlBPVU5EIiwiUk9PVE1FTlUiLCJQT1BNRU5VIiwiSU5GTyIsIlNFVFRJTkciLCJSRVRVUk4iLCJFWElUIiwiRUpFQ1QiLCJTVUJUSVRMRSIsIlRSQUNLIiwiU0VBUkNIIiwiWk9PTUlOIiwiWk9PTU9VVCIsIlNIT1ciLCJSRVBFQVQiLCJBX0IiLCJBTkdMRSIsIlBJUCIsIkZVTkNUSU9OQSIsIkZVTkNUSU9OQiIsIkZVTkNUSU9OQyIsIkZVTkNUSU9ORCJdfSwic3ViaXRlbXMiOltdfV19XX19fV19";
        Base64.Decoder decoder = Base64.getDecoder();
        String baseCompile = "";
        try {
            baseCompile = new String(decoder.decode(base), "UTF-8");
        } catch (Exception e) {
            System.out.println("baocuo");
        }
        FindJsonUtil fju = new FindJsonUtil();
        List<String> list = fju.format(baseCompile);


        for (String aa : list) {
            gizwitsService.handleProjectJson(aa);
        }*/
        GizwitsServiceImpl gizwitsService = new GizwitsServiceImpl();
        gizwitsService.getDeviceNameById("9","02d3084343bf");
    }


}
