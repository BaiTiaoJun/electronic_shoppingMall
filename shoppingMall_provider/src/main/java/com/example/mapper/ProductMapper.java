package com.example.mapper;

import com.example.domain.Product;
import com.example.vo.SplitPageRequestParamVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    int deleteByPrimaryKey(String pid);

    int insert(Product record);

    int insertSelective(Product record);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    /**
     * 查询所有的产品记录
     * @return
     */
    List<Product> selectProductList();

    /**
     * 通过条件分页查询商品
     * @param map
     * @return
     */
    List<Product> selectProductListBySplitPageSearch(Map<String, Object> map);

    /**
     * 根据商品的id获取单个商品信息
     * @param pid
     * @return
     */
    Product selectProductByPid(@Param("pid") String pid);

    /**
     * 根据类型查询商品的集合
     * @param type
     * @return
     */
    List<Product> selectProductListByType(@Param("type") String type);

    /**
     * 加入购物车业务：根据pid查询单个商品的信息
     * @param pid
     * @return
     */
    Product selectSingleProductByPid(String pid);

    /**
     * 根据商品系列id查询该id下的所有商品
     * @param sid
     * @return
     */
    List<Product> selectProductBySid(String sid);

    /**
     * 根据前台获取的pid查询出sid和商品单价
     * @param pid
     * @return
     */
    Product selectByPid(String pid);

    /**
     * 根据sid删除product
     * @param sid
     * @return
     */
    int deleteProductBySid(String sid);

    Product selectBySid(String sid);
}