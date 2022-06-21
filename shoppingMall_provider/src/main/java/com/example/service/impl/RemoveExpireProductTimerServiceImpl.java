package com.example.service.impl;

import com.example.domain.Cart;
import com.example.domain.ProductSeries;
import com.example.mapper.CartMapper;
import com.example.mapper.ProductMapper;
import com.example.mapper.ProductSeriesMapper;
import com.example.mapper.SkuMapper;
import com.example.service.RemoveExpireProductTimerService;
import com.example.util.ConstantUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @类名 RemoveExpireProductTimerServiceImpl
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/9 16:54
 * @版本 1.0
 */
@DubboService(interfaceClass = RemoveExpireProductTimerService.class, version = "1.0.0")
public class RemoveExpireProductTimerServiceImpl implements RemoveExpireProductTimerService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSeriesMapper productSeriesMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Override
    public void removeExpireProduct(String uid) throws Exception {
        //根据当前用户的uid查询购物车，得到购物车的cid
        Cart cart = cartMapper.selectCartByUid(uid);
        if (cart != null) {
            String cid = cart.getCid();
            //通过此cid遍历产品系列，得到产品系列的剩余商品数量和sid
            List<ProductSeries> productSeriesList = productSeriesMapper.selectProductSeriesListByCid(cid);
            for (ProductSeries productSeries : productSeriesList) {
                Integer available = productSeries.getAvaliable();
                String sid = productSeries.getSid();
                //如果判断此商品剩余数量为0，就删除sid所对应的product
                if (available == 0) {
                    int productRes = productMapper.deleteProductBySid(sid);
                    if (productRes == 0) {
                        throw new Exception(ConstantUtil.REMOVE_EXPIRE_PRODUCT_FAIL);
                    }
                    //删除sid所对应的sku
                    int skuRes = skuMapper.deleteSkuBySid(sid);
                    if (skuRes == 0) {
                        throw new Exception(ConstantUtil.REMOVE_EXPIRE_PRODUCT_FAIL);
                    }
                    //删除sid对应的product_series
                    int productSeriesRes = productSeriesMapper.deleteProductSeriesBySid(sid);
                    if (productSeriesRes == 0) {
                        throw new Exception(ConstantUtil.REMOVE_EXPIRE_PRODUCT_FAIL);
                    }
                }
            }
        }
    }
}
