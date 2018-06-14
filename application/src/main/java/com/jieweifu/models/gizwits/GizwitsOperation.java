package com.jieweifu.models.gizwits;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

/**
 * Created by 陶Lyn
 * on 2018/5/14.
 */
@SuppressWarnings("unused")
@Entity(tableName = "gizwits_operation")
public class GizwitsOperation {

    @Column(primaryKey = true)
    private Integer id;

    private String mac;

    private String did;

    private String jsonObject;

    private String createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
