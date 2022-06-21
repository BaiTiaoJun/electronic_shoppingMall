package com.example.domain;

import java.io.Serializable;

public class Sku implements Serializable {
    private static final long serialVersionUID = 7086553242870210716L;

    private String skuId;

    private String colorImage;

    private String setMeal;

    private String sid;

    private String cid;

    private Integer productNum;

    private String createTime;

    private String createBy;

    private Double singleTotalPrice;

    private String payOrderNo;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getColorImage() {
        return colorImage;
    }

    public void setColorImage(String colorImage) {
        this.colorImage = colorImage;
    }

    public String getSetMeal() {
        return setMeal;
    }

    public void setSetMeal(String setMeal) {
        this.setMeal = setMeal;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Double getSingleTotalPrice() {
        return singleTotalPrice;
    }

    public void setSingleTotalPrice(Double singleTotalPrice) {
        this.singleTotalPrice = singleTotalPrice;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }
}