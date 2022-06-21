package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @类名 DateUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/2 08:44
 * @版本 1.0
 */
public class DateUtil {

    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getDateTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
