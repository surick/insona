package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@SuppressWarnings("unused")
@Entity(tableName = "insona_product_sale")
public class ProductSale {
    @Column(primaryKey = true)
    private int id;
    @Column(columnName = "did")
    private String did;
    @Column(columnName = "name")
    private String name;
    @Column(columnName = "gizwit_info")
    private String gizwit_info;
    @Column(columnName = "insona_online")
    private String insona_online;
    @Column(columnName = "serial_code")
    private String serial_code;
    @Column(columnName = "last_online")
    private String last_online;
    @Column(columnName = "version")
    private String version;
    @Column(columnName = "sub_user")
    private String sub_user;
    @Column(columnName = "sub_home")
    private String sub_home;
    @Column(columnName = "address")
    private String address;
    @Column(columnName = "other")
    private String other;
    @Column(columnName = "near_status")
    private String near_status;
    @Column(columnName = "near_order")
    private String near_order;
    @Column(columnName = "near_event")
    private String near_event;
    @Column(columnName = "sub_inter")
    private String sub_inter;
    @Column(columnName = "sub_maker")
    private String sub_maker;
    @Column(columnName = "remark")
    private String remark;
    @Column(columnName = "sort_no")
    private String sort_no;
    @Column(columnName = "type")
    private String type;
    @Column(columnName = "status")
    private String status;
    @Column(columnName = "extract")
    private String extract;
    @Column(columnName = "dealer")
    private String dealer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getGizwit_info() {
        return gizwit_info;
    }

    public void setGizwit_info(String gizwit_info) {
        this.gizwit_info = gizwit_info;
    }

    public String getInsona_online() {
        return insona_online;
    }

    public void setInsona_online(String insona_online) {
        this.insona_online = insona_online;
    }

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }

    public String getLast_online() {
        return last_online;
    }

    public void setLast_online(String last_online) {
        this.last_online = last_online;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSub_user() {
        return sub_user;
    }

    public void setSub_user(String sub_user) {
        this.sub_user = sub_user;
    }

    public String getSub_home() {
        return sub_home;
    }

    public void setSub_home(String sub_home) {
        this.sub_home = sub_home;
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

    public String getNear_status() {
        return near_status;
    }

    public void setNear_status(String near_status) {
        this.near_status = near_status;
    }

    public String getNear_order() {
        return near_order;
    }

    public void setNear_order(String near_order) {
        this.near_order = near_order;
    }

    public String getNear_event() {
        return near_event;
    }

    public void setNear_event(String near_event) {
        this.near_event = near_event;
    }

    public String getSub_inter() {
        return sub_inter;
    }

    public void setSub_inter(String sub_inter) {
        this.sub_inter = sub_inter;
    }

    public String getSub_maker() {
        return sub_maker;
    }

    public void setSub_maker(String sub_maker) {
        this.sub_maker = sub_maker;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSort_no() {
        return sort_no;
    }

    public void setSort_no(String sort_no) {
        this.sort_no = sort_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }
}
