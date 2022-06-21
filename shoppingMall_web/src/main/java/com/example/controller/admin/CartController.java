package com.example.controller.admin;

import com.example.domain.User;
import com.example.service.CartService;
import com.example.service.RemoveExpireProductTimerService;
import com.example.util.ConstantUtil;
import com.example.vo.CartListVo;
import com.example.vo.CartResultVo;
import com.example.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @类名 CartController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/5 12:01
 * @版本 1.0
 */
@Controller
@RequestMapping("/admin")
public class CartController {

    @DubboReference(interfaceClass = CartService.class, version = "1.0.0")
    private CartService cartService;

    @DubboReference(interfaceClass = RemoveExpireProductTimerService.class, version = "1.0.0")
    private RemoveExpireProductTimerService removeExpireProductTimerService;

    @GetMapping("/queryCart.do")
    public String queryCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute(ConstantUtil.SESSION_USER);
        String uid = user.getUid();
        CartResultVo<CartListVo> cartResultVo = cartService.queryCart(uid);
        model.addAttribute("cartResultVo", cartResultVo);
        return "admin/cart";
    }

    @PostMapping("/addCart.do")
    public @ResponseBody ResultVo addCart(@RequestParam Map<String, Object> map, HttpSession session) {
        User user = (User) session.getAttribute(ConstantUtil.SESSION_USER);
        map.put("uid", user.getUid());
        try {
            String res = cartService.addCart(map);
            if (StringUtils.equals(res, ConstantUtil.PRODUCT_EXPIRE)) {
                removeExpireProductTimerService.removeExpireProduct(user.getUid());
                return new ResultVo(false, res);
            }
            return new ResultVo(true, res);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo(false, e.getMessage());
        }
    }

    @PostMapping("/removeCart.do")
    public @ResponseBody ResultVo removeCart(@RequestParam String pid,
                                             @RequestParam String skuid,
                                             HttpSession session) {
        User user = (User) session.getAttribute(ConstantUtil.SESSION_USER);
        String uid = user.getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("pid", pid);
        map.put("skuid", skuid);
        ResultVo resultVo = new ResultVo();
        try {
            cartService.removeCart(map);
            resultVo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            resultVo.setFlag(false);
            resultVo.setMessage(ConstantUtil.DELETE_CART_FAIL);
        }
        return resultVo;
    }

    @PostMapping("/editCart.do")
    public @ResponseBody ResultVo editCart(HttpSession session,
                                           @RequestParam String pid,
                                           @RequestParam String productNum,
                                           @RequestParam String skuid) {
        User user = (User) session.getAttribute(ConstantUtil.SESSION_USER);
        String uid = user.getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("pid", pid);
        map.put("productNum", productNum);
        map.put("skuid", skuid);
        try {
            cartService.editCart(map);
            return new ResultVo(true);
        } catch (Exception e) {
            return new ResultVo(false, ConstantUtil.EDIT_CART_FAIL);
        }
    }
}