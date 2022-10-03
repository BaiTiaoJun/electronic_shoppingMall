package com.example.mapper;

import com.example.domain.Cart;

import java.util.Map;

public interface CartMapper {
    int deleteByPrimaryKey(Integer cid);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer cid);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /**
     * 根据用户查询购物车信息，如果有说明之前创建过此购物车
     * @param uid
     * @return
     */
    Cart selectCartByUid(String uid);

    /**
     * 如果查询出来有此购物车，就根据购物车的id更新购物车信息
     * @param cart
     * @return
     */
    int updateCartByCart(Cart cart);

    /**
     * 根据uid删除此用户的购物车
     * @param uid
     * @return
     */
    int deleteByUid(String uid);

    /**
     * 修改购物车
     * @param map1
     * @return
     */
    int updateByMap1(Map<String, Object> map1);

    /**
     * 通过uid更新购物车信息
     * @param cart
     */
    int updateCartByUid(Cart cart);
}