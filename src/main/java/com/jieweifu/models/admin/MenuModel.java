package com.jieweifu.models.admin;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@Entity(tableName = "base_menu")
@SuppressWarnings("all")
public class MenuModel {

    @Column(primaryKey = true)
    private int id;


}
