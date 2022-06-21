package com.example.service;

import com.example.domain.Product;
import com.example.vo.SingleProductVo;
import com.example.vo.SplitPageRequestParamVo;
import com.github.pagehelper.PageInfo;

/**
 * @类名 ProductService
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/25 15:36
 * @版本 1.0
 */
public interface ProductService {
    /**
     * 查询所有的商品
     * @return
     */
    PageInfo<Product> queryProductList();

    /**
     * 根据条件分页查询商品
     * @param paramVo
     * @return
     */
    PageInfo<Product> queryProductListBySplitPageAndSearch(SplitPageRequestParamVo paramVo);

    /**
     * 根据pid查询单个产品具体信息
     * @param pid
     * @return
     */
    SingleProductVo queryProductByPid(String pid);

    /**
     * 生成商品详情页面的支付订单
     * @param price
     * @param num
     */
    String createPayOrderNo(Double price, Integer num, String payment) throws Exception;

    void generateOrder(String pid, String setMeal, String color, Integer num, Double totalPrice, String uid, String payOrderNo) throws Exception;
}
