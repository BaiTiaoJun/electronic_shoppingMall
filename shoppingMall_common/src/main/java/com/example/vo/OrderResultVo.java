package com.example.vo;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @类名 OrderResultVo
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/9 23:47
 * @版本 1.0
 */
public class OrderResultVo <T> implements Serializable {
    private static final long serialVersionUID = 137986000768286719L;
    private List<T> orderList;
    private Double checkOutPrice;
    private Double totalUnPaidPrice;
    private PageInfo<T> pageInfo;

    public OrderResultVo() {
    }

    public OrderResultVo(List<T> list) {
        this.orderList = list;
    }

    public OrderResultVo(List<T> orderList, Double checkOutPrice) {
        this.orderList = orderList;
        this.checkOutPrice = checkOutPrice;
    }

    public List<T> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<T> orderList) {
        this.orderList = orderList;
    }

    public Double getCheckOutPrice() {
        return checkOutPrice;
    }

    public void setCheckOutPrice(Double checkOutPrice) {
        this.checkOutPrice = checkOutPrice;
    }

    public void setTotalUnPaidPrice(Double totalUnPaidPrice) {
        this.totalUnPaidPrice = totalUnPaidPrice;
    }

    public Double getTotalUnPaidPrice() {
        return totalUnPaidPrice;
    }

    public void setPageInfo(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public PageInfo<T> getPageInfo() {
        return pageInfo;
    }
}
