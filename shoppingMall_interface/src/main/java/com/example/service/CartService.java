package com.example.service;

import com.example.domain.Cart;
import com.example.vo.CartResultVo;

import java.util.Map;

/**
 * @类名 CartService
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/6 21:11
 * @版本 1.0
 */
public interface CartService {
    /**
     * 添加商品到购物车
     * @param map
     * @throws Exception
     */
    String addCart(Map<String, Object> map) throws Exception;

    /**
     * 查询购物车的信息
     * @param uid
     * @return
     */
    CartResultVo queryCart(String uid);

    /**
     * 根据pid，skuid，uid对购物车进行删除
     * @param map
     */
    void removeCart(Map<String, Object> map) throws Exception;

    /**
     * 更新购物车的商品数量和单个商品总价
     * @param map
     * @throws Exception
     */
    void editCart(Map<String, Object> map) throws Exception;
}
