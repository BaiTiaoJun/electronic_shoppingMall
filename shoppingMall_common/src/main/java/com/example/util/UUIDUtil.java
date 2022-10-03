package com.example.util;

import java.util.UUID;

/**
 * @类名 UUIDUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/1 14:14
 * @版本 1.0
 */
public class UUIDUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
