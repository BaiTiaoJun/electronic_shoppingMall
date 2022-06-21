package com.example.vo;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @类名 ReturnVo
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/28 15:20
 * @版本 1.0
 */
public class SplitPageResponseResultVo<T> extends PageInfo<T> implements Serializable {

    private static final long serialVersionUID = 2938099915172947300L;
    private List<T> list;
    private PageInfo<T> pageInfo;
    private String selectType;
    private String searchText;
    private Integer maxPrice;
    private Integer minPrice;

    public SplitPageResponseResultVo() {
    }

    public SplitPageResponseResultVo(List<T> list, PageInfo<T> pageInfo) {
        this.list = list;
        this.pageInfo = pageInfo;
    }

    public SplitPageResponseResultVo(List<T> list, PageInfo<T> pageInfo, String selectType, String searchText) {
        this.list = list;
        this.pageInfo = pageInfo;
        this.selectType = selectType;
        this.searchText = searchText;
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public void setList(List<T> list) {
        this.list = list;
    }

    public PageInfo<T> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;
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