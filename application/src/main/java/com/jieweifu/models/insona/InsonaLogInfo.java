package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

/**
 * Created by 陶Lyn
 * on 2018/3/21.
 */
@Entity
public class InsonaLogInfo {

    @Column(columnName = "id")
    private int id  ;
    @Column(columnName = "opt")
    private String opt;
    @Column(columnName = "uri")
    private String uri;
    @Column(columnName = "action_time")
    private String actionTime;
    @Column(columnName = "crt_time")
    private String createTime;
    @Column(columnName = "crt_user")
    private Integer createUser;
    @Column(columnName = "username")
    private String createUserName;
    @Column(columnName = "crt_host")
    private String createHost;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }

}
