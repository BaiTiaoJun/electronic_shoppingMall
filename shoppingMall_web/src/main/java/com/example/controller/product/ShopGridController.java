package com.example.controller.product;

import com.example.domain.Product;
import com.example.service.ProductService;
import com.example.vo.SplitPageRequestParamVo;
import com.example.vo.SplitPageResponseResultVo;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @类名 GridController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/28 15:42
 * @版本 1.0
 */
@Controller
@RequestMapping("/product")
public class ShopGridController {

    @DubboReference(interfaceClass = ProductService.class, version = "1.0.0")
    private ProductService productService;

    @GetMapping("/queryProductListBySplitPage.do")
    public String queryProductListBySplitPage(SplitPageRequestParamVo paramVo, Model model) {
        PageInfo<Product> pageInfo = productService.queryProductListBySplitPageAndSearch(paramVo);
        SplitPageResponseResultVo<Product> responseResultVo = new SplitPageResponseResultVo<>(pageInfo.getList(), pageInfo, paramVo.getSelectType(), paramVo.getSearchText());
        responseResultVo.setMaxPrice(paramVo.getMaxPrice());
        responseResultVo.setMinPrice(paramVo.getMinPrice());
        model.addAttribute("responseResultVo", responseResultVo);
        return "product/shop-grid";
    }
}