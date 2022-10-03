package com.example.controller.admin;

import com.example.domain.User;
import com.example.service.RedisService;
import com.example.service.UserService;
import com.example.util.ConstantUtil;
import com.example.util.ShortLetterCodeUtil;
import com.example.vo.ResultVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @类名 registerController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/1 16:33
 * @版本 1.0
 */
@Controller
public class RegisterController {

    @DubboReference(interfaceClass = UserService.class, version = "1.0.0")
    private UserService userService;

    @DubboReference(interfaceClass = RedisService.class, version = "1.0.0")
    private RedisService redisService;

    @GetMapping("/admin/isExistUsername.do")
    public @ResponseBody ResultVo isExistUsername(@RequestParam String username) {
        String userName = userService.queryUserByUsername(username);
        ResultVo resultVo = new ResultVo();
        if (userName != null) {
            resultVo.setFlag(ConstantUtil.FLAG_FAIL);
            resultVo.setMessage("此用户名已被注册");
        } else {
            resultVo.setFlag(ConstantUtil.FLAG_SUCCESS);
        }
        return resultVo;
    }

    @PostMapping("/admin/requestShortLetterCode.do")
    public @ResponseBody ResultVo requestShortLetterCode(@RequestParam String phoneNumber) {
        try {
            Map<String, String> map = ShortLetterCodeUtil.getShortLetterCode(phoneNumber);
            if (StringUtils.equals(map.get("status"), "OK")) {
                redisService.put(phoneNumber, map.get("code"), 1);
                return new ResultVo(ConstantUtil.FLAG_SUCCESS);
            } else {
                return new ResultVo(ConstantUtil.FLAG_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo(ConstantUtil.FLAG_FAIL, ConstantUtil.SEND_SHORT_MESSAGE_SEND);
        }
    }

    @PostMapping("/admin/register.do")
    public @ResponseBody ResultVo register(@RequestParam Map<String, Object> map, HttpSession session) {
        String shortLetter = (String) map.get("shortLetter");
        String shortLetter2 = (String) redisService.get((String) map.get("phoneNumber"));
        if (StringUtils.equals(shortLetter, shortLetter2)) {
            try {
                User user = userService.saveUser(map);
                if (ObjectUtils.allNotNull(user)) {
                    session.setAttribute(ConstantUtil.SESSION_USER, user);
                    return new ResultVo(true);
                } else {
                    return new ResultVo(false, ConstantUtil.REGISTER_FAIL);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultVo(false, ConstantUtil.REGISTER_FAIL);
            }
        } else {
            return new ResultVo(false, ConstantUtil.VERIFY_CODE_ERROR);
        }
    }

    @GetMapping("/admin/isExistPhoneNumber.do")
    public @ResponseBody ResultVo isExistPhoneNumber(@RequestParam String phoneNumber) {
        String phoneNum = userService.queryUserByPhoneNumber(phoneNumber);
        if (ObjectUtils.allNull(phoneNum)) {
            return new ResultVo(true);
        } else {
            return new ResultVo(false, ConstantUtil.PHONE_NUMBER_REGISTERED_TIP);
        }
    }
}
