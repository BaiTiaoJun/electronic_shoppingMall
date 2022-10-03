package com.example.vo;

import java.io.Serializable;

/**
 * @类名 CartListVo
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/7 16:16
 * @版本 1.0
 */
public class CartListVo implements Serializable {
    private static final long serialVersionUID = 5010550078643224329L;
    private String image;
    private String name;
    private String color;
    private String setMeal;
    private Integer productSeriesOnCartNum;
    private Double singleProductPrice;
    private Double productSeriesTotalPrice;
    private String pid;
    private String skuid;
    private String createTime;
    private Integer available;


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    private String cid;

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

    public Integer getProductSeriesOnCartNum() {
        return productSeriesOnCartNum;
    }

    public void setProductSeriesOnCartNum(Integer productSeriesOnCartNum) {
        this.productSeriesOnCartNum = productSeriesOnCartNum;
    }

    public Double getSingleProductPrice() {
        return singleProductPrice;
    }

    public void setSingleProductPrice(Double singleProductPrice) {
        this.singleProductPrice = singleProductPrice;
    }

    public Double getProductSeriesTotalPrice() {
        return productSeriesTotalPrice;
    }

    public void setProductSeriesTotalPrice(Double productSeriesTotalPrice) {
        this.productSeriesTotalPrice = productSeriesTotalPrice;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }
}
