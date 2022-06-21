package com.example.util;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @类名 ShareShortLinkUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/20 22:39
 * @版本 1.0
 */
public class ShareShortLinkUtil {
    public static String shareShortLink(String targetURL) {
        String host = "https://shortlink.market.alicloudapi.com";
        String path = "/shortlink/create";
        String method = "POST";
        String appcode = "64ac953e7e79432fb2e6a92324132eb8";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("target", targetURL);

        String shortURL = null;
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //获取response的body
             shortURL = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shortURL;
    }
}
