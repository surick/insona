package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@SuppressWarnings("unused")
@Entity(tableName = "insona_document")
public class Document {
    @Column(primaryKey = true)
    private int id = -1;
    @NotBlank(message = "name不能为空")
    @Column(columnName = "name")
    private String name;
    @NotBlank(message = "图片不能为空")
    @Column(columnName = "file_url")
    private String fileUrl;
    @NotBlank(message = "设备不能为空")
    @Column(columnName = "file_type")
    private String fileType;
    @Min(value = 0, message = "排序不能为空")
    @Column(columnName = "sort_no")
    private Integer sortNo;
    @Column(columnName = "is_deleted")
    private Integer isDelete;
    @Column(columnName = "crt_time")
    private String createTime;
    @Column(columnName = "crt_user")
    private String createUser;
    @Column(columnName = "crt_name")
    private String createName;
    @Column(columnName = "crt_host")
    private String createHost;
    @Column(columnName = "upd_time")
    private String updateTime;
    @Column(columnName = "upd_user")
    private String updateUser;
    @Column(columnName = "upd_name")
    private String updateName;
    @Column(columnName = "upd_host")
    private String updateHost;
    private String attr1;
    private String attr2;
    private String attr3;
    private String attr4;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
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

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getUpdateHost() {
        return updateHost;
    }

    public void setUpdateHost(String updateHost) {
        this.updateHost = updateHost;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public String getAttr4() {
        return attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }
}
