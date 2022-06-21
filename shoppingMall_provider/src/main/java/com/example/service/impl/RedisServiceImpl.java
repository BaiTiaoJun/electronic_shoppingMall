package com.example.service.impl;

import com.example.service.RedisService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void put(String key, Object value, Integer expireTime) {
        ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        operations.set(key, value, expireTime, TimeUnit.MINUTES);
    }

    @Override
    public Object get(String key) {
        ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return operations.get(key);
    }

    @Override
    public void add(String key, Object... value) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        SetOperations<Object, Object> operations = redisTemplate.opsForSet();
        operations.add(key, value);
    }

    @Override
    public Object getSet(String key) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        SetOperations<Object, Object> operations = redisTemplate.opsForSet();
        return operations.members(key);
    }

    @Override
    public Set<Object> sdiff(String keyOnIneffective, String keyOnEffective) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        SetOperations<Object, Object> operations = redisTemplate.opsForSet();
        return operations.difference(keyOnIneffective, keyOnEffective);
    }

    @Override
    public void remove(String keyOnIneffective, Object... value) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        SetOperations<Object, Object> operations = redisTemplate.opsForSet();
        operations.remove(keyOnIneffective, value);
    }
}
