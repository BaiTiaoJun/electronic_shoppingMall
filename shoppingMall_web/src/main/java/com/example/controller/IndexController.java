package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @类名 IndexController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/27 17:16
 * @版本 1.0
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String toIndex() {
        return "forward:product/queryProductList.do";
    }
}