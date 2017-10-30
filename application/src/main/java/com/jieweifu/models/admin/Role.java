package com.jieweifu.models.admin;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@SuppressWarnings("unused")
@Entity(tableName = "base_role")
public class Role {
    @Column(primaryKey = true)
    private int id = -1;

    @Column(columnName = "role_name")
    private String roleName;

    @Column(columnName = "role_code")
    private String roleCode;

    @Column(columnName = "parent_id")
    private Integer parentId;

    @Column(columnName = "order_num")
    private String orderNum;

    private String description;
    private Integer enabled;

    @Column(columnName = "crt_time")
    private String createTime;

    @Column(columnName = "crt_user")
    private String createUser;

    @Column(columnName = "crt_name")
    private String createUserName;

    @Column(columnName = "crt_host")
    private String createHost;

    @Column(columnName = "upd_time")
    private String updateTime;

    @Column(columnName = "upd_user")
    private String updateUser;

    @Column(columnName = "upd_name")
    private String updateUserName;

    @Column(columnName = "upd_host")
    private String updateHost;

    private String attr1;
    private String attr2;
    private String attr3;
    private String attr4;
    private String attr5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
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

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
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

    public String getAttr5() {
        return attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }
}
