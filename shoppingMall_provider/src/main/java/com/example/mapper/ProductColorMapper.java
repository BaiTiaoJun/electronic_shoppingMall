package com.example.mapper;

import com.example.domain.ProductColor;

import java.util.List;

public interface ProductColorMapper {
    int deleteByPrimaryKey(String cid);

    int insert(ProductColor record);

    int insertSelective(ProductColor record);

    ProductColor selectByPrimaryKey(String cid);

    int updateByPrimaryKeySelective(ProductColor record);

    int updateByPrimaryKey(ProductColor record);

    List<ProductColor> selectByPid(String pid);
}