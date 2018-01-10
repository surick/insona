package com.jieweifu.common.business;

import com.jieweifu.constants.CommonConstant;
import com.jieweifu.constants.UserConstant;
import com.jieweifu.models.admin.User;

import java.time.Instant;
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

    public static void setUser(User user) {
        set(UserConstant.USER, user);
    }

    public static int getUserId() {
        return getUser().getId();
    }

    public static String getName() {
        return getUser().getName();
    }

    public static String getUserName() {
        return getUser().getUserName();
    }

    public static void setUserIsAdmin(boolean isAdmin) {
        set(UserConstant.USER_IS_ADMIN, isAdmin);
    }

    public static boolean getUserIsAdmin() {
        Object isAdmin = get(UserConstant.USER_IS_ADMIN);
        return isAdmin != null && (boolean) isAdmin;
    }

    public static String getRequestIp() {
        String requestIp = (String) get(CommonConstant.REQUEST_IP);
        if (requestIp == null) {
            return "未知";
        }
        return requestIp;
    }

    public static void setActionStartTime() {
        set(CommonConstant.ACTION_START_TIME, Instant.now());
    }

    public static void setActionEndTime() {
        set(CommonConstant.ACTION_END_TIME, Instant.now());
    }

    public static Instant getActionStartTime() {
        Instant instant = (Instant) get(CommonConstant.ACTION_START_TIME);
        if (instant == null) {
            instant = Instant.now();
        }
        return instant;
    }

    public static Instant getActionEndTime() {
        Instant instant = (Instant) get(CommonConstant.ACTION_END_TIME);
        if (instant == null) {
            instant = Instant.now();
        }
        return instant;
    }

    public static void setRequestIp(String ip) {
        set(CommonConstant.REQUEST_IP, ip);
    }

    public static User getUser() {
        User user = (User) get(UserConstant.USER);
        if (user == null) {
            user = new User();
            user.setUserName("未知");
            user.setId(-1);
        }
        return user;
    }
}
