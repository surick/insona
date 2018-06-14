package com.jieweifu.models.gizwits;

/**
 * Created by 陶Lyn
 * on 2018/5/3.
 */
public class Subitems {
    private Integer id;

    private  String type;

    private String name;

    private Object attributes;

    private Subitems subitems;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subitems getSubitems() {
        return subitems;
    }

    public void setSubitems(Subitems subitems) {
        this.subitems = subitems;
    }

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }
}
