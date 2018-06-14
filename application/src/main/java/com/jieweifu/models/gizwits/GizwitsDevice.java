package com.jieweifu.models.gizwits;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

import java.util.List;
import java.util.Map;

/**
 * Created by 陶Lyn
 * on 2018/4/28.
 */

public class GizwitsDevice {
    @Column(primaryKey = true)
    private Integer device_id;

    private Integer id;
    private List<Responses> responses;
    //private List<Responses> responses;
   // private Responses responses;
   // private Map<String,Object> responses;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Responses> getResponses() {
        return responses;
    }

    public void setResponses(List<Responses> responses) {
        this.responses = responses;
    }
}
