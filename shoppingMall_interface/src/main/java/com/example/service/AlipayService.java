package com.example.service;

import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.domain.User;

import java.util.Map;

/**
 * @类名 AlipayService
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/14 13:42
 * @版本 1.0
 */
public interface AlipayService {
    AlipayTradePagePayResponse pay(String payOrderNo, Double totalAmount, String pament, User user, String... skuid);

    String verify(Map<String, String> params, String outOrderNo);
}
