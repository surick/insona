package com.jieweifu.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

@SuppressWarnings("unused")
public class ResultModel {
    private int code = HttpStatus.OK.value();
    private boolean success = true;

    private String message;
    private Object data;

    public ResultModel setData(Object data) {
        this.data = data;
        return this;
    }

    public ResultModel setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResultModel setError(int code, String message) {
        ResultModel model = new ResultModel();
        this.code = code;
        this.message = message;
        this.success = false;
        return this;
    }

    public ResultModel setError(String message) {
        ResultModel model = new ResultModel();
        this.message = message;
        this.success = false;
        return this;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public String toJSON() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
