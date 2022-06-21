package com.example.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @类名 ShortLetterCodeUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/20 23:41
 * @版本 1.0
 */
public class ShortLetterCodeUtil {

    public static Map<String, String> getShortLetterCode(String phoneNumber) throws Exception {
        String url = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";

        String appCodeStr = ConstantUtil.APP_CODE;
        Map<String, String> appCode = new HashMap<>();
        appCode.put("Authorization", "APPCODE " + appCodeStr);

        Map<String, String> params = new HashMap<>();
        String code = CodeUtil.getCode(6);
        params.put("phone_number", phoneNumber);
        params.put("content", "code:" + code);
        params.put("template_id", ConstantUtil.SHORT_MESSAGE_TEMPLATE);

        HttpResponse httpResponse = HttpUtils.doPost(url, path, method, appCode, null, params);

        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(httpResponse.getEntity()));
        String status = jsonObject.getString("status");

        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("status", status);
        return map;
    }
}
