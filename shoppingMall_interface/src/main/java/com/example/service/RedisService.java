package com.example.service;

import java.util.Set;

/**
 * @类名 RedisService
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/1 14:04
 * @版本 1.0
 */
public interface RedisService {
    void put(String key, Object value, Integer expireTime);

    Object get(String key);

    void add(String key, Object... value);

    Object getSet(String key);

    Set<Object> sdiff(String keyOnIneffective, String keyOnEffective);

    void remove(String keyOnIneffective, Object... value);
}
