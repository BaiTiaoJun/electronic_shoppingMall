package com.example.controller.admin;

import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.domain.User;
import com.example.service.AlipayService;
import com.example.service.OrderService;
import com.example.service.PayOrderService;
import com.example.util.ConstantUtil;
import com.example.vo.OrderListVo;
import com.example.vo.OrderResultVo;
import com.example.vo.ResultVo;
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
 * @创建日期 2022/6/12 19:59
 * @版本 1.0
 */
@Controller
@RequestMapping("/admin")
public class OrderController {

    @DubboReference(interfaceClass = OrderService.class, version = "1.0.0")
    private OrderService orderService;

    @DubboReference(interfaceClass = PayOrderService.class, version = "1.0.0")
    private PayOrderService payOrderService;

    @DubboReference(interfaceClass = AlipayService.class, version = "1.0.0")
    private AlipayService alipayService;

    @GetMapping("/toOrder.do")
    public String toOrder(HttpSession session, Model model, Integer currentPage, Integer totalSize) {
        String uid = ((User) session.getAttribute(ConstantUtil.SESSION_USER)).getUid();
        OrderResultVo<OrderListVo> resultVo = null;

        try {
            resultVo = orderService.generateOrderList(uid, currentPage, totalSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("resultVo", resultVo);
        return "admin/order";
    }

    @GetMapping("/payUnPaidProduct.do")
    public String payUnPaidProduct(@RequestParam String[] oids,
                                                 HttpSession session,
                                                 Model model) {
        User user = (User) session.getAttribute(ConstantUtil.SESSION_USER);

        //根据用户提交的skuid生成支付用的订单信息：支付订单号、单笔支付总金额，用于提交支付
        Map<String, Object> map = null;
        try {
            map = payOrderService.createPayOrderForSingleUnpaidOrder(oids);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String payOrderNo = (String) map.get("payOrderNo");
        Double totalAmount = (Double) map.get("totalAmount");
        //暂时定义成支付宝支付方式
        String payment = ConstantUtil.ALI_PAY;

        //定义请求结果变量
        String result = "";

        //支付宝接口支付
        if (ObjectUtils.equals(payment, ConstantUtil.ALI_PAY)) {
            AlipayTradePagePayResponse response = alipayService.pay(payOrderNo, totalAmount, payment, user);
            if (response.isSuccess()) {
                try {
                    orderService.generateOrder(null, user.getUid(), payOrderNo);
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

    @PostMapping("/removeOrder.do")
    public @ResponseBody ResultVo removeOrder(@RequestParam String orderId) {
        try {
            orderService.removeOrder(orderId);
            return new ResultVo(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo(false, ConstantUtil.REMOVE_ORDER_FAIL);
        }
    }

    @PostMapping("/sureOrder.do")
    public @ResponseBody ResultVo sureOrder(@RequestParam String oid) {
        try {
            int res = orderService.sureOrder(oid);
            if (res == 0) {
                return new ResultVo(false, ConstantUtil.ORDER_STATUS_RECEIVED_FAIl);
            } else {
                return new ResultVo(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo(false, ConstantUtil.ORDER_STATUS_RECEIVED_FAIl);
        }
    }
}
