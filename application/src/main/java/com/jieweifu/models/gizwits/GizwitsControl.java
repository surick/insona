package com.jieweifu.models.gizwits;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

import java.util.Map;

/**
 * Created by 陶Lyn
 * on 2018/4/28.
 */

@SuppressWarnings("unused")
@Entity(tableName = "gizwits_control")
public class GizwitsControl {

    @Column(primaryKey = true)
    private int id;
    private String cmd;
    private String delivery_id;
    private String event_type;
    private String did;
    private float created_at;
    private String product_key;
    private String mac;
    private String group_id;
    private Map<String,String> data;


    public float getCreated_at() {
        return created_at;
    }

    public void setCreated_at(float created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }



    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }



    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
