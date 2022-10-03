package com.example.vo;

import org.springframework.ui.Model;

import java.io.Serializable;

/**
 * @类名 SplitPageRequestParamVo
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/25 17:00
 * @版本 1.0
 */
public class SplitPageRequestParamVo implements Serializable {
    private static final long serialVersionUID = -8678105783823935093L;
    private Integer totalSize;
    private Integer currentPage;
    private String selectType;
    private String searchText;
    private Integer maxPrice;
    private Integer minPrice;

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }
}
