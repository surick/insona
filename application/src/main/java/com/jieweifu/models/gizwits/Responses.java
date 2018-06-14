package com.jieweifu.models.gizwits;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by 陶Lyn
 * on 2018/4/28.
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Responses {
    private String name;
    private String sequence;
    private String ret;
    private Data data;

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
