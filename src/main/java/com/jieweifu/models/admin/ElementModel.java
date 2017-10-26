package com.jieweifu.models.admin;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@SuppressWarnings("unused")
@Entity(tableName = "base_element")
public class ElementModel {
    @Column(primaryKey = true)
    private int id = -1;

    private String code;
    private String type;
    @Column(columnName = "element_name")
    private String elementName;

    private String uri;
    @Column(columnName = "menu_id")
    private int menuId = -1;

    @Column(columnName = "parent_id")
    private int parentId = -1;

    private String path;
    private String method;
    private String description;

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
    private String attr6;
    private String attr7;
    private String attr8;
    private String attr9;
    private String attr10;
}
