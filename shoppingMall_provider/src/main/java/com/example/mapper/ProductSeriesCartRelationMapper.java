package com.example.mapper;

import com.example.domain.ProductSeriesCartRelation;
import org.apache.ibatis.annotations.Param;

public interface ProductSeriesCartRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProductSeriesCartRelation record);

    int insertSelective(ProductSeriesCartRelation record);

    ProductSeriesCartRelation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProductSeriesCartRelation record);

    int updateByPrimaryKey(ProductSeriesCartRelation record);

    /**
     * 查询有没有此商品系列和购物车关系
     * @param cid
     * @return
     */
    ProductSeriesCartRelation selectSeriesCartRelationByCid(@Param("cid") String cid, @Param("series") String series);

    /**
     * 根据sid个cid删除product_series_cart_relation表
     * @param sid
     * @param cid
     * @return
     */
    int deleteBySidAndCid(@Param("sid") String sid, @Param("cid") String cid);
}