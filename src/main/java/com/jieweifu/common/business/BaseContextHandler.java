package com.jieweifu.common.business;

import com.jieweifu.constants.CommonConstant;
import com.jieweifu.models.admin.UserModel;

import java.util.HashMap;
import java.util.Map;

public class BaseContextHandler {
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    /**
     * 防止内存溢出, 需调用此方法
     */
    public static void remove() {
        threadLocal.remove();
    }

    /**
     * 将数据存入到ThreadLocal
     */
    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    /**
     * 返回存入到ThreadLocal的值
     */
    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static void setUser(UserModel userModel) {
        set(CommonConstant.USER, userModel);
    }

    public static UserModel getUser() {
        return (UserModel) get(CommonConstant.USER);
    }
}
