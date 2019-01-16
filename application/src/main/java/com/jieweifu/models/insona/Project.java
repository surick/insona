package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;
import com.jieweifu.models.regex.Regex;

import javax.validation.constraints.Pattern;

/**
 * @author Jin
 * @date 2018/9/28下午2:06
 */
@SuppressWarnings("unused")
@Entity(tableName = "insona_project")
public class Project {
    @Column(primaryKey = true)
    private int id;

    @Pattern(regexp = Regex.DID,message = "设备did格式错误")
    @Column(columnName = "did")
    private String did;

    private String mac;

    private String project;

    private String createTime;

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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
