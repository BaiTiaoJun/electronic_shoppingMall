package com.example.mapper;

import com.example.domain.DicValue;

import java.util.List;

public interface DicValueMapper {
    int deleteByPrimaryKey(String id);

    int insert(DicValue record);

    int insertSelective(DicValue record);

    DicValue selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DicValue record);

    int updateByPrimaryKey(DicValue record);

    /**
     * 根据code的类型查询dicvalue的记录
     * @param typeCode
     * @return
     */
    List<Object> selectDicValueByTypeCode(String typeCode);
}