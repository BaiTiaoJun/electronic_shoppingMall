package com.example.vo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @类名 SingleProductVo
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/30 20:09
 * @版本 1.0
 */
public class SingleProductVo <T1, T2, T3, T4> implements Serializable {
    private static final long serialVersionUID = 8865344906086137844L;
    private T1 product;
    private Integer count;
    private String percentage;
    private List<T2> remarks;
    private List<T1> products;
    private String[] images;
    private String[] detailImages;
    private List<T3> productColorList;
    private List<T4> productSetMealList;
    private String[] imageList;

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public List<T1> getProducts() {
        return products;
    }

    public void setProducts(List<T1> products) {
        this.products = products;
    }

    public T1 getProduct() {
        return product;
    }

    public void setProduct(T1 product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public List<T2> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<T2> remarks) {
        this.remarks = remarks;
    }

    public List<T3> getProductColorList() {
        return productColorList;
    }

    public void setProductColorList(List<T3> productColorList) {
        this.productColorList = productColorList;
    }

    public List<T4> getProductSetMealList() {
        return productSetMealList;
    }

    public void setProductSetMealList(List<T4> productSetMealList) {
        this.productSetMealList = productSetMealList;
    }

    public String[] getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String[] detailImages) {
        this.detailImages = detailImages;
    }

    public String[] getImageList() {
        return imageList;
    }

    public void setImageList(String[] imageList) {
        this.imageList = imageList;
    }
}
