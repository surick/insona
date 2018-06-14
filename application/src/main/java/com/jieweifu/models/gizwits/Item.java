package com.jieweifu.models.gizwits;

import java.util.List;

/**
 * Created by 陶Lyn
 * on 2018/5/3.
 */
public class Item {
    private Integer id;
    private String type;
    private String name;
    private Attributes attributes;
    private List<Subitems> subitems;

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

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<Subitems> getSubitems() {
        return subitems;
    }

    public void setSubitems(List<Subitems> subitems) {
        this.subitems = subitems;
    }
}
