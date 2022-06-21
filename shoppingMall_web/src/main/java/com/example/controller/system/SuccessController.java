package com.example.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @类名 SuccessController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/12 16:41
 * @版本 1.0
 */
@Controller
@RequestMapping("/system")
public class SuccessController {

    @GetMapping("/toSuccess.do")
    public String toSuccess() {
        return "system/success";
    }
}
