package com.example.vo;

import java.io.Serializable;

/**
 * @类名 OrderListVo
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/9 23:49
 * @版本 1.0
 */
public class OrderListVo implements Serializable {
    private static final long serialVersionUID = 8299197866899761968L;
    private String image;
    private String name;
    private String oid;
    private String color;
    private String setMeal;
    private String brand;
    private String orderStatus;
    private Integer singleProductTotalNum;
    private String payStatus;
    private Double singleProductTotalPrice;
    private Double singleProductPrice;
    private String createTime;
    private String skuid;
    private String pid;
    private Integer available;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSetMeal() {
        return setMeal;
    }

    public void setSetMeal(String setMeal) {
        this.setMeal = setMeal;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getSingleProductTotalNum() {
        return singleProductTotalNum;
    }

    public void setSingleProductTotalNum(Integer singleProductTotalNum) {
        this.singleProductTotalNum = singleProductTotalNum;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Double getSingleProductTotalPrice() {
        return singleProductTotalPrice;
    }

    public void setSingleProductTotalPrice(Double singleProductTotalPrice) {
        this.singleProductTotalPrice = singleProductTotalPrice;
    }

    public void setSingleProductPrice(Double singleProductPrice) {
        this.singleProductPrice = singleProductPrice;
    }

    public Double getSingleProductPrice() {
        return singleProductPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }
}
