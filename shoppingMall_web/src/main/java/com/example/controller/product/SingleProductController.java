package com.example.controller.product;

import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.domain.User;
import com.example.service.AlipayService;
import com.example.service.ProductService;
import com.example.util.ConstantUtil;
import com.example.util.ShareShortLinkUtil;
import com.example.vo.SingleProductVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @类名 SingleProductController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/29 17:35
 * @版本 1.0
 */
@Controller
@RequestMapping("/product")
public class SingleProductController {

    @DubboReference(interfaceClass = ProductService.class, version = "1.0.0")
    private ProductService productService;

    @DubboReference(interfaceClass = AlipayService.class, version = "1.0.0")
    private AlipayService alipayService;

    @GetMapping("/toSingleProduct.do")
    public String toSingleProduct(@RequestParam String pid, Model model) {
        SingleProductVo productVo = productService.queryProductByPid(pid);
        model.addAttribute("singleProduct", productVo);
        return "product/single-product";
    }

    @GetMapping("/pay.do")
    public String pay(@RequestParam String pid,
                      @RequestParam Double price,
                      @RequestParam Integer num,
                      @RequestParam String setMeal,
                      @RequestParam String color,
                      HttpSession session,
                      Model model) {
        User user = ((User) session.getAttribute(ConstantUtil.SESSION_USER));
        //用户id
        String uid = user.getUid();
        //支付类型
        String payment = ConstantUtil.ALI_PAY;
        //支付订单
        String payOrderNo = "";
        try {
            payOrderNo = productService.createPayOrderNo(price, num, payment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //计算支付总金额
        Double totalAmount = num * price;
        //支付
        String result = "";
        if (ObjectUtils.equals(payment, ConstantUtil.ALI_PAY)) {
            AlipayTradePagePayResponse response = alipayService.pay(payOrderNo, totalAmount, payment, user);
            if (response.isSuccess()) {
                try {
                    productService.generateOrder(pid, setMeal, color, num, totalAmount, uid, payOrderNo);
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

    @PostMapping("/shareProduct.do")
    public @ResponseBody String shareProduct(@RequestParam String targetURL) {
        return ShareShortLinkUtil.shareShortLink(targetURL);
    }
}
