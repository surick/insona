package com.jieweifu.models.admin;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@SuppressWarnings("unused")
@Entity(tableName = "base_role_authority")
public class RoleAuthorityModel {
    @Column(primaryKey = true)
    private int id = -1;

    @Column(columnName = "role_id")
    private int roleId = -1;

    @Column(columnName = "resource_id")
    private int resourceId = -1;

    @Column(columnName = "resource_type")
    private String resourceType;

    private String description;
    private String path;

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
}
