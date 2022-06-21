package com.example.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @类名 CartResultVo
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/7 01:09
 * @版本 1.0
 */
public class CartResultVo<T> implements Serializable {
    private static final long serialVersionUID = 7595077332394865984L;
    List<T> list;

    private String cid;

    private Double cartTotalPrice;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Double getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(Double cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
