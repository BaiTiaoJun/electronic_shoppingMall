package com.example.service.impl;

import com.example.mapper.DicValueMapper;
import com.example.service.DicValueService;
import com.example.util.ConstantUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 * @类名 DicValueServiceImpl
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/27 16:59
 * @版本 1.0
 */
@DubboService(interfaceClass = DicValueService.class, version = "1.0.0")
public class DicValueServiceImpl implements DicValueService {

    @Autowired
    private DicValueMapper dicValueMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Object> queryDicValueType(String typeCode) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();

        List<Object> dicValues = operations.range(typeCode, 0, -1);

        assert dicValues != null;
        if (dicValues.size() == 0) {
            synchronized (this) {
                dicValues = operations.range(typeCode, 0, -1);
                assert dicValues != null;
                if (dicValues.size() == 0) {
                    dicValues = dicValueMapper.selectDicValueByTypeCode(typeCode);
                    for (Object dicValue : dicValues) {
                        operations.rightPush(typeCode, dicValue);
                    }
                }
            }
        }
        return dicValues;
    }

    @Override
    public List<Object> queryDicTypeByDicType(String typeCodeProductType) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();

        List<Object> objects = operations.range(ConstantUtil.PRODUCT_TYPE_KEY, 0, -1);
        assert objects != null;
        if (objects.size() == 0) {
            synchronized (this) {
                objects = operations.range(ConstantUtil.PRODUCT_TYPE_KEY, 0, -1);
                assert objects != null;
                if (objects.size() == 0) {
                    List<Object> dicTypes =  dicValueMapper.selectDicValueByTypeCode(typeCodeProductType);
                    for (Object dicType:dicTypes) {
                        operations.leftPush(ConstantUtil.PRODUCT_TYPE_KEY, dicType);
                    }
                }
            }
        }
        return objects;
    }
}
