package com.jieweifu.models.gizwits;

import com.jieweifu.common.dbservice.Entity;

/**
 * Created by 陶Lyn
 * on 2018/5/4.
 */
@SuppressWarnings("unused")
@Entity(tableName = "gizwits_device")
public class Gizwits_Device {
    private Integer device_id;

    private Integer id;
    private String name;
    private String sequence;
    private String ret;

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }
}
