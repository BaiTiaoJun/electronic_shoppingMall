package com.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.domain.PayOrder;
import com.example.domain.User;
import com.example.mapper.PayOrderMapper;
import com.example.properties.AlipayProperties;
import com.example.service.AlipayService;
import com.example.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Objects;

/**
 * @类名 AlipayServiceImpl
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/14 13:41
 * @版本 1.0
 */
@DubboService(interfaceClass = AlipayService.class, version = "1.0.0")
@Slf4j
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private AlipayProperties alipayProperties;

    @Override
    public AlipayTradePagePayResponse pay(String payOrderNo, Double totalAmount, String payment, User user, String... skuid) {
        AlipayClient alipayClient = new DefaultAlipayClient(alipayProperties.getGatewayUrl(),
                alipayProperties.getApp_id(),
                alipayProperties.getMerchant_private_key(),
                alipayProperties.getFormat(),
                alipayProperties.getCharset(),
                alipayProperties.getAlipay_public_key(),
                alipayProperties.getSign_type());

        //封装参数
        //订单信息
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", payOrderNo);//商户订单号，必填
        bizContent.put("total_amount", totalAmount);//付款金额，必填
        bizContent.put("subject", ConstantUtil.ORDER_NAME);//订单名称，必填
        bizContent.put("product_code", ConstantUtil.PRODUCT_CODE);//产品码
        bizContent.put("timeout_express", ConstantUtil.TIMEOUT_EXPRESS); //交易超时时间，超过这个时间交易失效

        //买家信息
        bizContent.put("name", user.getName());//指定买家姓名
        bizContent.put("mobile", user.getPhone());//指定买家手机号
        /**
         * 指定买家证件类型
         * IDENTITY_CARD：身份证；
         * PASSPORT：护照；
         * OFFICER_CARD：军官证；
         * SOLDIER_CARD：士兵证；
         * HOKOU：户口本
         */
//        bizContent.put("cert_type", ConstantUtil.CERT_TYPE);//指定买家证件类型
//        bizContent.put("cert_no", user.getIdCard());//买家证件号
        /**
         * 是否强制校验买家信息；
         * 需要强制校验传：T;
         * 不需要强制校验传：F或者不传；
         */
        bizContent.put("need_check_info", ConstantUtil.NEED_CHECK_INFO);//是否强制校验买家信息

        //创建request,设置请求参数
        AlipayTradePagePayRequest PayRequest = new AlipayTradePagePayRequest();
        PayRequest.setReturnUrl(alipayProperties.getReturn_url()); //设置同步回调地址
        PayRequest.setNotifyUrl(alipayProperties.getNotify_url()); //设置异步回调地址
        PayRequest.setBizContent(bizContent.toString());

        //返回请求表单
        AlipayTradePagePayResponse response = null;
        try {
            //接口请求
            response = alipayClient.pageExecute(PayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public String verify(Map<String, String> params, String outOrderNo) {
        /**
         * params – 待验签的从支付宝接收到的参数Map
         * alipayPublic – 支付宝公钥字符串
         * charset – 参数内容编码集
         * signType – 指定采用的签名方式，RSA或RSA2
         * Returns:
         * true：验签通过；false：验签不通过
         */
        String result = ConstantUtil.TRADE_FAIL;

        boolean isSign = false;
        try {
            isSign = AlipaySignature.rsaCheckV1(params, alipayProperties.getAlipay_public_key(), alipayProperties.getCharset(),alipayProperties.getSign_type());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (!isSign) {
            log.error(ConstantUtil.VERIFY_FAIL_LOG);
            return result;
        }
        /**
         * 商家需要验证该通知数据中的 out_order_no 是否为商家系统中创建的支付订单号。
         * 判断 total_amount 是否确实为本次操作的金额。
         * seller_id是否是outOrderNo这笔订单对应的商户
         * app_id是否是商户本身
         * 校验通知中的out_order_no、out_request_no 是否是商户系统里的订单号
         */
        PayOrder payOrder = payOrderMapper.selectByPrimaryKey(outOrderNo);

        if (ObjectUtils.allNull(payOrder)) {
            log.error(ConstantUtil.VERIFY_FAIL_LOG);
            return result;
        }

        String totalAmount2 = params.get("total_amount");

        Double totalAmount = payOrder.getTotalAmount();
        if (!Objects.equals(totalAmount, Double.valueOf(totalAmount2))) {
            log.error(ConstantUtil.VERIFY_FAIL_LOG);
            return result;
        }

        String sellerId = params.get("seller_id");
        if (!sellerId.equals(alipayProperties.getSellerPid())) {
            log.error(ConstantUtil.VERIFY_FAIL_LOG);
            return result;
        }

        String appId = params.get("app_id");
        if (!appId.equals(alipayProperties.getApp_id())) {
            log.error(ConstantUtil.VERIFY_FAIL_LOG);
            return result;
        }

        String status = params.get("trade_status");
        if (!StringUtils.equals(status, ConstantUtil.TRADE_SUCCESS)) {
            log.error(ConstantUtil.TRADE_FAIL_INFO);
            return result;
        }
        return ConstantUtil.TRADE_SUCCESS;
    }
}
