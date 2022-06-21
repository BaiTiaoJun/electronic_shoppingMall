package com.example.mapper;

import com.example.domain.ProductSeries;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductSeriesMapper {
    int deleteByPrimaryKey(String sid);

    int insert(ProductSeries record);

    int insertSelective(ProductSeries record);

    ProductSeries selectByPrimaryKey(String sid);

    int updateByPrimaryKeySelective(ProductSeries record);

    int updateByPrimaryKey(ProductSeries record);

    /**
     * 根据系列编号和提交的商品数量更改商品剩余数量
     * @param map
     * @return
     */
    int editProductSeriesAvailableBySeries(Map<String, Object> map);

    /**
     * 根据购物车的cid查询有所与之相关的的商品系列
     * @param cid
     * @return
     */
    List<ProductSeries> selectProductSeriesListByCid(String cid);

    /**
     * 通过sid作为条件，把前端传递的商品数量在商品系列中做比较
     * @param sid
     * @param productNum
     * @return
     */
    ProductSeries selectBySidAndProductNum(@Param("sid") String sid, @Param("productNum") Integer productNum);

    int deleteProductSeriesBySid(String sid);
}