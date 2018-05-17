package com.jieweifu.models.gizwits;

import java.util.Map;

/**
 * Created by 陶Lyn
 * on 2018/4/28.
 */
public class GizwitsJson {
    private String cmd;
    private String deliver_id;
    private String event_type;
    private String did;
    private String created_at;
    private String product_key;
    private String mac;
    private String group_id;
    private Map<String,String> data;


    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getDeliver_id() {
        return deliver_id;
    }

    public void setDeliver_id(String deliver_id) {
        this.deliver_id = deliver_id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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
