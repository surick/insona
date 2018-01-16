package com.jieweifu.common.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * redis封装工具集
 */
@SuppressWarnings("SpellCheckingInspection")
@Service
public class RedisUtil {

    @Value("${custom.redis.prefix}")
    private String redisPrefix;

    private RedissonClient redissonClient;
    private RedisOperations<String, Object> redisOperations;

    @Autowired
    public RedisUtil(RedissonClient redissonClient, RedisOperations<String, Object> redisOperations) {
        this.redissonClient = redissonClient;
        this.redisOperations = redisOperations;
    }

    public void lock(String lockName, int timeout, Runnable successAction, Runnable errorAction) {
        RLock lock = redissonClient.getFairLock(getKey(lockName));
        try {
            boolean res = lock.tryLock(timeout * 10, timeout, TimeUnit.SECONDS);
            if (res) {
                successAction.run();
            } else {
                errorAction.run();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                lock.unlock();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getKey(String key) {
        return redisPrefix + key;
    }

    public void set(String key, Object value) {
        key = getKey(key);
        redisOperations.opsForValue().set(key, value);
    }

    public void setEX(String key, Object value, int timeout, TimeUnit timeUnit) {
        key = getKey(key);
        redisOperations.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void incr(String key, int value){
        key = getKey(key);
        redisOperations.opsForValue().increment(key, value);
    }

    public Object get(String key) {
        key = getKey(key);
        return redisOperations.opsForValue().get(key);
    }

    public void delete(String key) {
        key = getKey(key);
        if (redisOperations.hasKey(key)) {
            redisOperations.delete(key);
        }
    }

    public void expiry(String key, int timeout, TimeUnit timeUnit) {
        key = getKey(key);
        redisOperations.expire(key, timeout, timeUnit);
    }

    public boolean hasKey(String key) {
        key = getKey(key);
        return redisOperations.hasKey(key);
    }
}
