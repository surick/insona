package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

/**
 * Created by 陶Lyn
 * on 2018/3/16.
 */
@SuppressWarnings("unused")
@Entity(tableName = "insona_operation")
public class InsonaOperation {

    @Column(primaryKey = true)
    private int id = -1;

    private String name;

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
}
