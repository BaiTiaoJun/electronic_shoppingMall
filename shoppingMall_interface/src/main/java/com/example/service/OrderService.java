package com.example.service;

import com.example.domain.Order;
import com.example.vo.OrderListVo;
import com.example.vo.OrderResultVo;

import java.util.List;

/**
 * @类名 OrderService
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/9 22:56
 * @版本 1.0
 */
public interface OrderService {
    void generateOrder(String[] skuid, String uid, String payOrderNo) throws Exception;

    OrderResultVo<OrderListVo> generateTempOrder(String[] skuid, Double totalAmount);

    void processOrderAndProduct(String outOrderNo, String uid) throws Exception;

    OrderResultVo<OrderListVo> generateOrderList(String uid, Integer currentPage, Integer pageSize) throws Exception;

    String queryOrderByPayOrderNo(String outOrderNo);

    void removeOrder(String orderId) throws Exception;

    int sureOrder(String oid);
}
