package com.example.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.example.domain.User;
import com.example.service.RedisService;
import com.example.service.UserService;
import com.example.util.ConstantUtil;
import com.example.util.HttpUtils;
import com.example.util.ImageCodeUtil;
import com.example.util.UUIDUtil;
import com.example.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @类名 loginController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/31 23:11
 * @版本 1.0
 */
@Controller
public class LoginController {

    @DubboReference(interfaceClass = UserService.class, version = "1.0.0")
    private UserService userService;

    @DubboReference(interfaceClass = RedisService.class, version = "1.0.0")
    private RedisService redisService;

    @PostMapping("/admin/login.do")
    public @ResponseBody ResultVo login(@RequestParam Map<String, Object> map, HttpSession session) {
        String messageCode = (String) map.get("messageCode");

        String imageCodeKey = String.valueOf(session.getAttribute(ConstantUtil.IMAGE_CODE_KEY));
        String imageCode = (String) redisService.get(imageCodeKey);

        if (StringUtils.equalsIgnoreCase(messageCode, imageCode)) {
            session.removeAttribute(imageCodeKey);

            User user = userService.queryUserByMap(map);
            if (!ObjectUtils.isEmpty(user)) {
                session.setAttribute(ConstantUtil.SESSION_USER, user);
                return new ResultVo(ConstantUtil.FLAG_SUCCESS);
            } else {
                return new ResultVo(ConstantUtil.FLAG_FAIL, ConstantUtil.LOGIN_MESSAGE_FAIL);
            }
        } else {
            session.removeAttribute(imageCodeKey);
            return new ResultVo(ConstantUtil.FLAG_FAIL, ConstantUtil.LOGIN_MESSAGE_FAIL);
        }
    }

    @GetMapping("/admin/loginOut.do")
    public String loginOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/admin/loadImageCode.do")
    public @ResponseBody ResultVo loadImageCode(HttpSession session) {

        ResultVo resultVo = new ResultVo();
        try {
            Map<String, String> map = ImageCodeUtil.getImageCode();
            String imageCodeKey = UUIDUtil.getUUID();
            redisService.put(imageCodeKey, map.get("text"), 1);
            session.setAttribute(ConstantUtil.IMAGE_CODE_KEY, imageCodeKey);
            resultVo.setMessage(map.get("img_path"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVo;
    }
}
