package com.jieweifu.common.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("SpellCheckingInspection")
@Service
public class RedisUtil {

    @Value("${custom.redis.prefix}")
    private String redisPrefix;

    private StringRedisTemplate redisTemplate;

    private RedissonClient redissonClient;

    @Autowired
    public RedisUtil(StringRedisTemplate redisTemplate, RedissonClient redissonClient) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
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

    public void set(String key, String value) {
        key = getKey(key);
        redisTemplate.opsForValue().set(key, value);
    }

    public void setEX(String key, String value, int timeout, TimeUnit timeUnit) {
        key = getKey(key);
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void incr(String key, int value){
        key = getKey(key);
        redisTemplate.opsForValue().increment(key, value);
    }

    public String get(String key) {
        key = getKey(key);
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        key = getKey(key);
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    public void expiry(String key, int timeout, TimeUnit timeUnit) {
        key = getKey(key);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public boolean hasKey(String key) {
        key = getKey(key);
        return redisTemplate.hasKey(key);
    }
}
