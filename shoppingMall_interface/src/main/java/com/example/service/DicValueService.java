package com.example.service;

import com.example.domain.DicValue;

import java.util.List;

/**
 * @类名 DicValueService
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/27 16:57
 * @版本 1.0
 */
public interface DicValueService {
    List<Object> queryDicValueType(String productType);

    /**
     * 根据字典类型查询相关类型的信息
     * @param typeCodeProductType
     * @return
     */
    List<Object> queryDicTypeByDicType(String typeCodeProductType);
}
