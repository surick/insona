package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@SuppressWarnings("unused")
@Entity(tableName = "insona_product")
public class Product {
    @Column(columnName = "did")
    private String did;
    @Column(columnName = "name")
    private String name;
    @Column(columnName = "gizwit_info")
    private String gizwitInfo;
    @Column(columnName = "insona_online")
    private int insonaOnline;
    @Column(columnName = "serial_code")
    private String serialCode;
    @Column(columnName = "last_online")
    private String lastOnline;
    @Column(columnName = "version")
    private String version;
    @Column(columnName = "sub_user")
    private String subUser;
    @Column(columnName = "sub_home")
    private String subHome;
    @Column(columnName = "address")
    private String address;
    @Column(columnName = "other")
    private String other;
    @Column(columnName = "near_status")
    private String nearStatus;
    @Column(columnName = "near_order")
    private String nearOrder;
    @Column(columnName = "near_event")
    private String nearEvent;
    @Column(columnName = "sub_inter")
    private String subInter;
    @Column(columnName = "sub_maker")
    private String subMaker;
    @Column(columnName = "remark")
    private String remark;
    @Column(columnName = "sort_no")
    private String sortNo;
    @Column(columnName = "type")
    private String type;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGizwitInfo() {
        return gizwitInfo;
    }

    public void setGizwitInfo(String gizwitInfo) {
        this.gizwitInfo = gizwitInfo;
    }

    public int getInsonaOnline() {
        return insonaOnline;
    }

    public void setInsonaOnline(int insonaOnline) {
        this.insonaOnline = insonaOnline;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubUser() {
        return subUser;
    }

    public void setSubUser(String subUser) {
        this.subUser = subUser;
    }

    public String getSubHome() {
        return subHome;
    }

    public void setSubHome(String subHome) {
        this.subHome = subHome;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getNearStatus() {
        return nearStatus;
    }

    public void setNearStatus(String nearStatus) {
        this.nearStatus = nearStatus;
    }

    public String getNearOrder() {
        return nearOrder;
    }

    public void setNearOrder(String nearOrder) {
        this.nearOrder = nearOrder;
    }

    public String getNearEvent() {
        return nearEvent;
    }

    public void setNearEvent(String nearEvent) {
        this.nearEvent = nearEvent;
    }

    public String getSubInter() {
        return subInter;
    }

    public void setSubInter(String subInter) {
        this.subInter = subInter;
    }

    public String getSubMaker() {
        return subMaker;
    }

    public void setSubMaker(String subMaker) {
        this.subMaker = subMaker;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
