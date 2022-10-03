package com.example.controller.admin;

import com.example.domain.User;
import com.example.service.RedisService;
import com.example.service.UserService;
import com.example.util.ConstantUtil;
import com.example.util.FastDFSUtil;
import com.example.util.FileUtil;
import com.example.util.QiniuyunFileUploadUtil;
import com.example.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("用户图片上传")
    @PostMapping(value = "/uploadProfile.do")
    public @ResponseBody ResultVo uploadProfile(@RequestBody MultipartFile profile, HttpSession session) {
        //获取文件名后缀
        String suffix = FileUtil.generateSuffix(Objects.requireNonNull(profile.getOriginalFilename()));
        try {

            //七牛云三方文件系统
//            key = QiniuyunFileUploadUtil.uploadFile(profile.getBytes(), fileName);
//            redisService.add(ConstantUtil.UPLOAD_IMAGE_NAME_SET, fileName);

            //fastdfs部署的文件系统
            byte[] bytes = profile.getBytes();
            String[] fileResource = FastDFSUtil.upload(bytes, suffix);
            if (fileResource == null) {
                throw new Exception();
            }
            //获取文件名
            String fileName = fileResource[0] + "/" + fileResource[1];
            //把文件名称添加到redis
            session.setAttribute("fileName", fileName);
            redisService.add(ConstantUtil.UPLOAD_IMAGE_NAME_SET, fileName);
            return new ResultVo(true, null, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVo(false, ConstantUtil.UPLOAD_IMAGE_FAIL);
    }

    @ApiOperation("操作用户所要保存成功的图片")
    @PostMapping(value = "/saveProfile.do")
    public @ResponseBody ResultVo saveProfile(@RequestParam String uid, HttpSession session) {
        String fileName = (String) session.getAttribute("fileName");
        if (ObjectUtils.allNotNull(fileName)) {
            try {
                int res = userService.editProfile(fileName, uid);
                if (res != 0) {
                    //将文件名称存储到key为saveImageNameSet的redis库中
                    redisService.add(ConstantUtil.SAVE_IMAGE_NAME_SET, fileName);
                    //更新用户状态
                    User user = (User) session.getAttribute(ConstantUtil.SESSION_USER);
                    user.setImage(fileName);
                    session.setAttribute(ConstantUtil.SESSION_USER, user);
                    //从session中删除fileName文件
                    session.removeAttribute("fileName");
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