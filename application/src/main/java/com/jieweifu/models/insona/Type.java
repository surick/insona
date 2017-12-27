package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;
import org.hibernate.validator.constraints.NotBlank;

@SuppressWarnings("unused")
@Entity(tableName = "insona_product_type")
public class Type {
    @Column(primaryKey = true)
    private int id;
    @NotBlank(message = "类型id不能为空")
    @Column(columnName = "type_id")
    private String type_id;
    @NotBlank(message = "类型不能为空")
    @Column(columnName = "type_name")
    private String type_name;
    @NotBlank(message = "生产商不能为空")
    @Column(columnName = "maker")
    private String maker;
    @NotBlank(message = "请填写型号")
    @Column(columnName = "model_no")
    private String model_no;
    @Column(columnName = "make_time")
    private String make_time;
    @NotBlank(message = "请填写生产批号")
    @Column(columnName = "make_no")
    private String make_no;
    @Column(columnName = "into_time")
    private String into_time;
    @NotBlank(message = "请填写操作人")
    @Column(columnName = "person")
    private String person;
    @NotBlank(message = "请填写技术方案")
    @Column(columnName = "technology")
    private String technology;
    @NotBlank(message = "请填写通讯方式")
    @Column(columnName = "communication")
    private String communication;
    @Column(columnName = "enable")
    private boolean enable;
    @Column(columnName = "is_deleted")
    private boolean is_deleted;
    @Column(columnName = "remark")
    private String remark;

    @Column(columnName = "crt_time")
    private String createTime;

    @Column(columnName = "crt_host")
    private String createHost;

    @Column(columnName = "upd_time")
    private String updateTime;

    @Column(columnName = "upd_host")
    private String updateHost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel_no() {
        return model_no;
    }

    public void setModel_no(String model_no) {
        this.model_no = model_no;
    }

    public String getMake_time() {
        return make_time;
    }

    public void setMake_time(String make_time) {
        this.make_time = make_time;
    }

    public String getMake_no() {
        return make_no;
    }

    public void setMake_no(String make_no) {
        this.make_no = make_no;
    }

    public String getInto_time() {
        return into_time;
    }

    public void setInto_time(String into_time) {
        this.into_time = into_time;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateHost() {
        return updateHost;
    }

    public void setUpdateHost(String updateHost) {
        this.updateHost = updateHost;
    }
}
