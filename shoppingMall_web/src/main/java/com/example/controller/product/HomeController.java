package com.example.controller.product;

import com.example.domain.Product;
import com.example.service.DicValueService;
import com.example.service.ProductService;
import com.example.util.ConstantUtil;
import com.example.vo.SplitPageRequestParamVo;
import com.example.vo.SplitPageResponseResultVo;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @类名 IndexController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/25 11:14
 * @版本 1.0
 */
@Controller
@RequestMapping("/product")
public class HomeController {

    @DubboReference(interfaceClass = ProductService.class, version = "1.0.0")
    private ProductService productService;

    @DubboReference(interfaceClass = DicValueService.class, version = "1.0.0")
    private DicValueService dicValueService;

    @GetMapping(value = "/queryProductList.do")
    public String queryHomeInfo(Model model) {
        PageInfo<Product> pageInfo = productService.queryProductList();
        List<Object> objects = dicValueService.queryDicTypeByDicType(ConstantUtil.TYPE_CODE_PRODUCT_TYPE);
        model.addAttribute(ConstantUtil.PRODUCT_TYPE_KEY, objects);
        model.addAttribute("productList", pageInfo.getList());
        return "product/home";
    }

    @GetMapping(value = "/queryProductListBySearch.do")
    public String queryProductListBySplitPage(Model model, SplitPageRequestParamVo paramVo) {
        PageInfo<Product> pageInfo = productService.queryProductListBySplitPageAndSearch(paramVo);

        SplitPageResponseResultVo<Product> responseResultVo = new SplitPageResponseResultVo<>(pageInfo.getList(),
                pageInfo, paramVo.getSelectType(), paramVo.getSearchText());

        if (paramVo.getMinPrice() != null && paramVo.getMaxPrice() != null) {
            responseResultVo.setMinPrice(paramVo.getMinPrice());
            responseResultVo.setMaxPrice(paramVo.getMaxPrice());
        }

        model.addAttribute("responseResultVo", responseResultVo);
        return "product/shop-grid";
    }
}