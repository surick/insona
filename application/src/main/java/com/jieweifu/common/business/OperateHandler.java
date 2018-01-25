package com.jieweifu.common.business;

import java.lang.reflect.Method;
import java.time.Instant;

public class OperateHandler {
    private String getUserId() {
        return String.valueOf(BaseContextHandler.getUserId());
    }

    private String getUserName() {
        return BaseContextHandler.getName();
    }

    private String getOperateIp() {
        return BaseContextHandler.getRequestIp();
    }

    private String getOperateTime() {
        return String.valueOf(Instant.now().toEpochMilli());
    }

    public static <T> void assignCreateUser(T model) {
        OperateHandler operateHandler = new OperateHandler();
        Method[] methods = model.getClass().getDeclaredMethods();
        try {
            for (Method method : methods) {
                switch (method.getName()) {
                    case "setCreateTime":
                        method.invoke(model, operateHandler.getOperateTime());
                        break;
                    case "setCreateUser":
                        method.invoke(model, operateHandler.getUserName());
                        break;
                    case "setCreateUserName":
                        method.invoke(model, operateHandler.getUserName());
                        break;
                    case "setCreateHost":
                        method.invoke(model, operateHandler.getOperateIp());
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static <T> void assignUpdateUser(T model) {
        OperateHandler operateHandler = new OperateHandler();
        Method[] methods = model.getClass().getDeclaredMethods();
        try {
            for (Method method : methods) {
                switch (method.getName()) {
                    case "setUpdateTime":
                        method.invoke(model, operateHandler.getOperateTime());
                        break;
                    case "setUpdateUser":
                        method.invoke(model, operateHandler.getUserName());
                        break;
                    case "setUpdateUserName":
                        method.invoke(model, operateHandler.getUserName());
                        break;
                    case "setUpdateHost":
                        method.invoke(model, operateHandler.getOperateIp());
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
