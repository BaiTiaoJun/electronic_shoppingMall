package com.example.mapper;

import com.example.domain.PayOrder;

public interface PayOrderMapper {
    int deleteByPrimaryKey(String payOrderNo);

    int insert(PayOrder record);

    int insertSelective(PayOrder record);

    PayOrder selectByPrimaryKey(String payOrderNo);

    int updateByPrimaryKeySelective(PayOrder record);

    int updateByPrimaryKey(PayOrder record);

    PayOrder selectBySkuid(String skuid);

    int updateByPayOrderNo(PayOrder payOrder);
}