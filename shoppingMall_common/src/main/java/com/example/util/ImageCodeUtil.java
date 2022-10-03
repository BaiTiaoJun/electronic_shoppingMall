package com.example.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @类名 ImageCodeUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/20 23:59
 * @版本 1.0
 */
public class ImageCodeUtil {
    public static Map<String, String> getImageCode() throws Exception {
        String host = "http://ali-make-mark.showapi.com";
        String path = "/make-mark-img";
        String method = "GET";

        Map<String, String> appCode = new HashMap<>();
        appCode.put("Authorization", "APPCODE " + ConstantUtil.APP_CODE);

        Map<String, String> params = new HashMap<>();
        // 图片边框
        params.put("border", "no");
        // 图片宽高
        params.put("image_height", "40");
        params.put("image_width", "80");
        /**
         * 图片样式
         * 水纹 com.google.code.kaptcha.impl.WaterRipple
         * 鱼眼 com.google.code.kaptcha.impl.FishEyeGimpy
         * 阴影 com.google.code.kaptcha.impl.ShadowGimpy
         */
        params.put("obscurificator_impl", "com.google.code.kaptcha.impl.WaterRipple");
        // 字体长度
        params.put("textproducer_char_length", "4");
        // 字体间隔
        params.put("textproducer_char_space", "2");
        // 从此集合中生成图片文本
        params.put("textproducer_char_string", ConstantUtil.TEXT_LIST);
        // 字体的颜色
        params.put("textproducer_font_color", "black");
        // 字体
        params.put("textproducer_font_names", "textproducer_font_names");
        // 字体大小
        params.put("textproducer_font_size", "30");

        String img_path = "";

        HttpResponse httpResponse = HttpUtils.doGet(host, path, method, appCode, params);

        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(httpResponse.getEntity()));
        String res_body = jsonObject.getString("showapi_res_body");

        JSONObject body = JSONObject.parseObject(res_body);
        img_path = body.getString("img_path");
        String text = body.getString("text");

        Map<String, String> map = new HashMap<>();
        map.put("img_path", img_path);
        map.put("text", text);
        return map;
    }
}
