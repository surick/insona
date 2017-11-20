package com.jieweifu.models.insona;


import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@Entity
public class ProductInfo {
    @Column(columnName = "id")
    private Integer id;
    @Column(columnName = "did")
    private String did;
    @Column(columnName = "insona_online")
    private Integer online;
    @Column(columnName = "is_disabled")
    private Integer disabled;
    @Column(columnName = "dev_alias")
    private String alias;
    @Column(columnName = "base_user_id")
    private String uid;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id=" + id +
                ", did='" + did + '\'' +
                ", online=" + online +
                ", disabled=" + disabled +
                ", alias='" + alias + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
