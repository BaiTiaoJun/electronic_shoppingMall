package com.example.mapper;

import com.example.domain.ProductSetMeal;

import java.util.List;

public interface ProductSetMealMapper {
    int deleteByPrimaryKey(String sid);

    int insert(ProductSetMeal record);

    int insertSelective(ProductSetMeal record);

    ProductSetMeal selectByPrimaryKey(String sid);

    int updateByPrimaryKeySelective(ProductSetMeal record);

    int updateByPrimaryKey(ProductSetMeal record);

    List<ProductSetMeal> selectByPid(String pid);
}