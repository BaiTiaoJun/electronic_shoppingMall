package com.example.controller.admin;

import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.domain.User;
import com.example.service.AlipayService;
import com.example.service.OrderService;
import com.example.service.PayOrderService;
import com.example.util.ConstantUtil;
import com.example.vo.OrderListVo;
import com.example.vo.OrderResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @类名 OrderController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/9 20:51
 * @版本 1.0
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class CheckOutController {

    @DubboReference(interfaceClass = OrderService.class, version = "1.0.0")
    private OrderService orderService;

    @DubboReference(interfaceClass = PayOrderService.class, version = "1.0.0")
    private PayOrderService payOrderService;

    @DubboReference(interfaceClass = AlipayService.class, version = "1.0.0")
    private AlipayService alipayService;

    @GetMapping("/generateTempOrder.do")
    public String generateTempOrder(Model model,
                                @RequestParam String[] skuid,
                                @RequestParam Double totalAmount) {
        OrderResultVo<OrderListVo> resultVo = orderService.generateTempOrder(skuid, totalAmount);
        model.addAttribute("resultVo", resultVo);
        return "admin/checkout";
    }

//    @GetMapping("/queryCheckOut.do")
//    public String queryCheckOut(HttpSession session, Model model) {
//        String uid = ((User) session.getAttribute(ConstantUtil.SESSION_USER)).getUid();
//        OrderResultVo<OrderListVo> resultVo = orderService.queryOrder(uid);
//        model.addAttribute("resultVo", resultVo);
//        return "admin/checkout";
//    }

    @GetMapping(value = "/pay.do", produces = "text/html;charset=utf-8")
    public String pay (@RequestParam String[] skuid,
                       @RequestParam Double totalAmount,
                       @RequestParam String payment,
                       HttpSession session,
                       Model model) {
        User user = (User) session.getAttribute(ConstantUtil.SESSION_USER);

        //根据用户提交的skuid生成支付用的订单信息，用于提交支付
        String payOrderNo = null;
        try {
            payOrderNo = payOrderService.createPayOrder(skuid, totalAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //支付宝接口支付
        String result = "";
        if (ObjectUtils.equals(payment, ConstantUtil.ALI_PAY)) {
            AlipayTradePagePayResponse response = alipayService.pay(payOrderNo, totalAmount, payment, user, skuid);
            if (response.isSuccess()) {
            //支付提交成功后生成订单，删除除了sku相关的购物车相关纪录
             try {
                 orderService.generateOrder(skuid, user.getUid(), payOrderNo);
             } catch (Exception e) {
                 e.printStackTrace();
             }
             result = response.getBody();
            }
        } else if (ObjectUtils.equals(payment, ConstantUtil.WECHAT_PAY)) {

        } else if (ObjectUtils.equals(payment, ConstantUtil.UNION_PAY)) {

        }

        model.addAttribute("result", result);
        return "system/requestForm";
    }

    @PostMapping("/tradeNotify.do")
    public @ResponseBody Object tradeNotify(@RequestParam Map<String, String> params) {
        //校验回调接口发回的信息
        String outOrderNo = params.get("out_trade_no");
        String result = alipayService.verify(params, outOrderNo);
        if (result.equals(ConstantUtil.TRADE_SUCCESS)) {
            /**
             * 完成所有验证，更新母、子订单的订单状态，更新产品的剩余数量
             * 通过outOrderNo查询order的创建人
             */
            String uid = orderService.queryOrderByPayOrderNo(outOrderNo);
            try {
                orderService.processOrderAndProduct(outOrderNo, uid);
                return "success";
            } catch (Exception e) {
                return result;
            }
        }
        return result;
    }
}
