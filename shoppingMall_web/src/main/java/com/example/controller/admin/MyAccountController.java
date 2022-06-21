package com.example.controller.admin;

import com.example.domain.User;
import com.example.service.RedisService;
import com.example.service.UserService;
import com.example.util.ConstantUtil;
import com.example.util.FileUtil;
import com.example.util.QiniuyunFileUploadUtil;
import com.example.vo.ResultVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @类名 myAccountController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/3 19:27
 * @版本 1.0
 */
@Controller
@RequestMapping("/admin")
public class MyAccountController {

    private String fileName;
    private String key;

    @DubboReference(interfaceClass = UserService.class, version = "1.0.0")
    private UserService userService;

    @DubboReference(interfaceClass = RedisService.class, version = "1.0.0")
    private RedisService redisService;

    //@CookieValue("code") String code参数中使用cookievalue注解获取前段请求的cookie
    @GetMapping("/toMyAccount.do")
    public String toMyAccount(Model model, @RequestParam String uid) {
        User user = userService.queryUserByUid(uid);
        model.addAttribute("user", user);
        return "admin/my-account";
    }

    @PostMapping(value = "/uploadProfile.do")
    public @ResponseBody ResultVo uploadProfile(@RequestBody MultipartFile profile) {
        //获取文件原来的名称
        String originalFileName = profile.getOriginalFilename();
        //生成文件名称
        fileName = FileUtil.generateFileName(Objects.requireNonNull(originalFileName));
        try {
            key = QiniuyunFileUploadUtil.uploadFile(profile.getBytes(), fileName);
            redisService.add(ConstantUtil.UPLOAD_IMAGE_NAME_SET, fileName);
            return new ResultVo(true, null, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVo(false, ConstantUtil.UPLOAD_IMAGE_FAIL);
    }

    @PostMapping(value = "/saveProfile.do")
    public @ResponseBody ResultVo saveProfile(@RequestParam String uid, HttpSession session) {
        if (ObjectUtils.allNotNull(key)) {
            try {
                int res = userService.editProfile(fileName, uid);
                if (res != 0) {
                    redisService.add(ConstantUtil.SAVE_IMAGE_NAME_SET, fileName);
                    User user = (User) session.getAttribute(ConstantUtil.SESSION_USER);
                    user.setImage(fileName);
                    session.setAttribute(ConstantUtil.SESSION_USER, user);
                    return new ResultVo(true, ConstantUtil.SAVE_IMAGE_SUCCESS);
                } else {
                    return new ResultVo(false, ConstantUtil.SAVE_IMAGE_FAIL);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultVo(false, ConstantUtil.SAVE_IMAGE_FAIL);
            }
        } else {
            return new ResultVo(false, ConstantUtil.SAVE_IMAGE_FAIL);
        }
    }
}