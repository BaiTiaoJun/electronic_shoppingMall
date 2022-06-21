package com.example.service.impl;

import com.example.domain.Order;
import com.example.domain.PayOrder;
import com.example.domain.Sku;
import com.example.mapper.OrderMapper;
import com.example.mapper.PayOrderMapper;
import com.example.mapper.SkuMapper;
import com.example.service.PayOrderService;
import com.example.util.ConstantUtil;
import com.example.util.DateUtil;
import com.example.util.SnowFlakeUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @类名 PayOrderServiceImpl
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/11 17:14
 * @版本 1.0
 */
@DubboService(interfaceClass = PayOrderService.class, version = "1.0.0")
public class PayOrderServiceImpl implements PayOrderService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public String createPayOrder(String[] skuid, Double totalAmount) throws Exception {
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderNo(String.valueOf(SnowFlakeUtil.getDefaultSnowFlakeId()));
        payOrder.setCreateTime(DateUtil.getDateTime());
        payOrder.setTotalAmount(totalAmount);
        payOrder.setPayStatus(ConstantUtil.PAY_STATUS_UNPAID);
        payOrder.setPayType(ConstantUtil.ALI_PAY);
        int resPayOrder = payOrderMapper.insertSelective(payOrder);
        if (resPayOrder == 0) {
            throw new Exception(ConstantUtil.PAY_STATUS_PAID_FAIL);
        }

        String payOrderNo = payOrder.getPayOrderNo();

        for (String id : skuid) {
            Sku sku = new Sku();
            sku.setSkuId(id);
            sku.setPayOrderNo(payOrderNo);
            int resSku = skuMapper.updateByPrimaryKeySelective(sku);
            if (resSku == 0) {
                throw new Exception(ConstantUtil.PAY_STATUS_PAID_FAIL);
            }
        }

        return payOrderNo;
    }

    @Override
    public PayOrder queryPayOrder(String[] skuids) {
        return payOrderMapper.selectBySkuid(skuids[0]);
    }

    @Override
    @Transactional
    public Map<String, Object> createPayOrderForSingleUnpaidOrder(String[] oids) throws Exception {
        String payOrderNo = String.valueOf(SnowFlakeUtil.getDefaultSnowFlakeId());
        Double totalPrice = 0.00;

        Order order;
        for (String oid : oids) {
            //根据oid查询order
            order = orderMapper.selectByPrimaryKey(oid);
            //计算提交的所有订单的总金额
            totalPrice += order.getTotalPrice();
            //关联支付订单和商品订单
            order = new Order();
            order.setOid(oid);
            order.setPayOrderNo(payOrderNo);
            int orderRes = orderMapper.updateByPrimaryKeySelective(order);
            if (orderRes == 0) {
                throw new Exception();
            }
        }
        //生成payorder表，返回payOrderNo
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderNo(payOrderNo);
        payOrder.setPayType(ConstantUtil.ALI_PAY);
        payOrder.setCreateTime(DateUtil.getDateTime());
        payOrder.setTotalAmount(totalPrice);
        payOrder.setPayStatus(ConstantUtil.PAY_STATUS_UNPAID);
        int payOrderRes = payOrderMapper.insertSelective(payOrder);
        if (payOrderRes == 0) {
            throw new Exception();
        }
        //封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("totalAmount", totalPrice);
        map.put("payOrderNo", payOrderNo);
        return map;
    }
}
