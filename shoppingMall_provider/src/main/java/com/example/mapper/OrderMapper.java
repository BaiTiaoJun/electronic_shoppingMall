package com.example.mapper;

import com.example.domain.Order;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(String oid);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    /**
     * 根据uid查询order集合
     * @param uid
     * @return
     */
    List<Order> selectOrderByUid(String uid);

    /**
     * 可显示color、setMeal文本内容的的订单查询
     * @param uid
     * @return
     */
    List<Order> selectOrderListByUid(String uid);

    int updateByPayOrderNo(Order order);

    List<Order> selectByPayOrderNo(String outOrderNo);

    int countOrdersByUid(String uid);
}