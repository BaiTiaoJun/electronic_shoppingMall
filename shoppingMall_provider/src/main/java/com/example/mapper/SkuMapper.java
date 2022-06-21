package com.example.mapper;

import com.example.domain.Sku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkuMapper {
    int deleteByPrimaryKey(String skuId);

    int insert(Sku record);

    int insertSelective(Sku record);

    Sku selectByPrimaryKey(String skuId);

    int updateByPrimaryKeySelective(Sku record);

    int updateByPrimaryKey(Sku record);

    /**
     * 通过商品系列的sid和购物车的cid，查询查询关联商品所有的sku属性
     * @param sid
     * @return
     */
    List<Sku> selectSkuListByCidAndSid(@Param("sid") String sid, @Param("cid") String cid);

    /**
     * 根据sid和cid查询出sku表查记录序列是否长度为0
     * @param cid
     * @param sid
     * @return
     */
    List<Sku> selectSkuByCidAndSid(@Param("sid") String sid, @Param("cid") String cid);

    int deleteSkuBySid(String sid);

    /**
     * 通过skuid查询sku信息
     * @param id
     * @return
     */
    Sku selectSkuBySkuId(String id);
}