package com.example.service.impl;

import com.example.service.RedisService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @类名 RedisServiceImpl
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/1 14:05
 * @版本 1.0
 */
@DubboService(interfaceClass = RedisService.class, version = "1.0.0")
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValueOperations<String, Object> operationsValue;

    @Autowired
    private SetOperations<String, Object> operationsSet;

    @Override
    public void put(String key, Object value, Integer expireTime) {
        operationsValue.set(key, value, expireTime, TimeUnit.MINUTES);
    }

    @Override
    public Object get(String key) {
        return operationsValue.get(key);
    }

    @Override
    public void add(String key, Object... value) {
        operationsSet.add(key, value);
    }

    @Override
    public Object getSet(String key) {
        return operationsSet.members(key);
    }

    @Override
    public Set<Object> sdiff(String keyOnIneffective, String keyOnEffective) {
        return operationsSet.difference(keyOnIneffective, keyOnEffective);
    }

    @Override
    public void remove(String keyOnIneffective, Object... value) {
        operationsSet.remove(keyOnIneffective, value);
    }
}
