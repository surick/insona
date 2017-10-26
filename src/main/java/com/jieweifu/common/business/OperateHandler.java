package com.jieweifu.common.business;

import com.jieweifu.constants.CommonConstant;

import java.time.Instant;

public class OperateHandler {
    public String getUserId() {
        return (String) BaseContextHandler.get(CommonConstant.USER_ID);
    }

    public String getUserName() {
        return (String) BaseContextHandler.get(CommonConstant.USER_NAME);
    }

    public String getOperateIp() {
        return (String) BaseContextHandler.get(CommonConstant.REQUEST_IP);
    }

    public String getOperateTime() {
        return String.valueOf(Instant.now().getEpochSecond());
    }
}
