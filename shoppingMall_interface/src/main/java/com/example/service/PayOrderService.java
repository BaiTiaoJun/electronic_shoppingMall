package com.example.service;

import com.example.domain.PayOrder;

import java.util.Map;

/**
 * @类名 PayOrderService
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/11 17:10
 * @版本 1.0
 */
public interface PayOrderService {
    String createPayOrder(String[] skuid, Double totalAmount) throws Exception;

    PayOrder queryPayOrder(String[] skuid);

    Map<String, Object> createPayOrderForSingleUnpaidOrder(String[] oids) throws Exception;
}
