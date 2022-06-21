package com.example.controller.components;

import com.example.domain.Product;
import com.example.service.DicValueService;
import com.example.service.ProductService;
import com.example.util.ConstantUtil;
import com.example.vo.SplitPageRequestParamVo;
import com.example.vo.SplitPageResponseResultVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @类名 HeaderController
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/27 17:10
 * @版本 1.0
 */

@Controller
@RequestMapping("/components")
public class HeaderController {
    @DubboReference(interfaceClass = DicValueService.class, version = "1.0.0")
    private DicValueService dicValueService;

    @DubboReference(interfaceClass = ProductService.class, version = "1.0.0")
    private ProductService productService;

    @GetMapping("/queryProductType.do")
    public @ResponseBody List<Object> queryProductType() {
        return dicValueService.queryDicValueType(ConstantUtil.TYPE_CODE_PRODUCT_TYPE);
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
