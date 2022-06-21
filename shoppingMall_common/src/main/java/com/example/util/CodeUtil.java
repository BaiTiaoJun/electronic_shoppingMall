package com.example.util;

import java.util.Random;

/**
 * @类名 CodeUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/2 00:29
 * @版本 1.0
 */
public class CodeUtil {

    public static String getCode(Integer digit) {
        Random random = new Random();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < digit; i++) {
            builder.append(random.nextInt(digit));
        }
        return builder.toString();
    }
}
