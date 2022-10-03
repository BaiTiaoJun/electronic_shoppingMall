package com.example.service.impl;

import com.example.domain.*;
import com.example.mapper.*;
import com.example.service.CartService;
import com.example.util.ConstantUtil;
import com.example.util.DateUtil;
import com.example.util.UUIDUtil;
import com.example.vo.CartListVo;
import com.example.vo.CartResultVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @类名 CartServiceImpl
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/6 21:14
 * @版本 1.0
 */
@DubboService(interfaceClass = CartService.class, version = "1.0.0")
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSeriesMapper productSeriesMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductSeriesCartRelationMapper productSeriesCartRelationMapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public String addCart(Map<String, Object> map) throws Exception {
        String pid = (String) map.get("pid");
        Integer num = Integer.valueOf((String) map.get("num"));
        //商品的pid查询此商品的系列编号、单价
        Product product = productMapper.selectSingleProductByPid(pid);
        //通过product得到商品系列编号，查询此商品数量是否为0，为0就过期，不可添加
        String series = product.getSeries();
        ProductSeries productSeries = productSeriesMapper.selectByPrimaryKey(series);
        Integer available = productSeries.getAvaliable();
        if (available > 0) {
            //根据数量和单价计算总价
            Double newPrice = product.getNewPrice();
            Double totalPrice = newPrice * num;
            //添加购物车，根据用户的uid判断此购物车记录是否存在，不存在则创建一个，如果存在就直接更新购物车的数据
            String uid = (String) map.get("uid");
            Cart cart = cartMapper.selectCartByUid(uid);
            int cartRes;
            if (ObjectUtils.isEmpty(cart)) {
                cart = new Cart();
                cart.setCid(UUIDUtil.getUUID());
                cart.setProductNumber(num);
                cart.setProductTotalPrice(totalPrice);
                cart.setCreateTime(DateUtil.getDate());
                cart.setCreateBy(uid);
                cart.setCartStatus(ConstantUtil.ORDER_STATUS_UNSUBMIT);
                cartRes = cartMapper.insert(cart);
                if (cartRes == 0) {
                    throw new Exception(ConstantUtil.ADD_CART_FAIL);
                }
            } else {
                cart.setProductNumber(num);
                cart.setProductTotalPrice(totalPrice);
                cart.setUpdateTime(DateUtil.getDate());
                cartRes = cartMapper.updateCartByCart(cart);
            }
            if (cartRes == 0) {
                throw new Exception(ConstantUtil.ADD_CART_FAIL);
            }
            //根据商品的id添加商品的sku（用户选择的影响商品价格的属性）
            Sku sku = new Sku();
            sku.setSkuId(UUIDUtil.getUUID());
            sku.setSetMeal((String) map.get("setMealId"));
            sku.setColorImage((String) map.get("colorId"));
            sku.setSid(series);
            sku.setCid(cart.getCid());
            sku.setProductNum(num);
            sku.setCreateTime(DateUtil.getDateTime());
            sku.setCreateBy(uid);
            sku.setSingleTotalPrice(totalPrice);
            int skuRes = skuMapper.insert(sku);
            if (skuRes == 0) {
                throw new Exception(ConstantUtil.ADD_CART_FAIL);
            }
            //添加购物车和商品系列的关联关系表
            //查询有没有此商品系列和购物车关系，如果没有就创建，否则不创建
            ProductSeriesCartRelation productSeriesCartRelation = productSeriesCartRelationMapper.selectSeriesCartRelationByCid(cart.getCid(), series);
            if (ObjectUtils.isEmpty(productSeriesCartRelation)) {
                productSeriesCartRelation = new ProductSeriesCartRelation();
                productSeriesCartRelation.setId(UUIDUtil.getUUID());
                productSeriesCartRelation.setCid(cart.getCid());
                productSeriesCartRelation.setSid(series);
                int productSeriesCartRelationRes = productSeriesCartRelationMapper.insert(productSeriesCartRelation);
                if (productSeriesCartRelationRes == 0) {
                    throw new Exception(ConstantUtil.ADD_CART_FAIL);
                }
            }
            return ConstantUtil.ADD_CART_SUCCESS;
        } else {
            return ConstantUtil.PRODUCT_EXPIRE;
        }
    }

    @Override
    public CartResultVo<CartListVo> queryCart(String uid) {
        List<CartListVo> cartResultVoList = new ArrayList<>();
        //通过uid查询购物车信息，一个user对应一个cart，获取每个商品的cart中的记录：总价、购物车的商品总量、购物车cid
        Cart cart = cartMapper.selectCartByUid(uid);
        if (ObjectUtils.allNotNull(cart)) {
            String cid = cart.getCid();
            Double cartTotalPrice = cart.getProductTotalPrice();
            //通过购物车的cid查询系列sid，当前用户购物车和商品系列是一对多关系,通过cid可以查询到多个sid，也就是一个购物车包含多个商品系列
            List<ProductSeries> productSeriesList = productSeriesMapper.selectProductSeriesListByCid(cid);
            //通过sid查询商品信息：商品名称、单价，一个商品系列可以得到多个同系列商品
            CartListVo cartListVo = null;
            for (ProductSeries productSeries : productSeriesList) {
                String sid = productSeries.getSid();
                Integer available = productSeries.getAvaliable();
                //通过商品系列的sid得到同系列每个商品id、名称、单价、图片
                List<Product> productList = productMapper.selectProductBySid(sid);
                for (Product product : productList) {
                    String name = product.getName();
                    Double singleProductPrice = product.getNewPrice();
                    String image = product.getImage();
                    String pid = product.getPid();
                    //通过商品系列的sid和查询商品的sku属性，商品系列和商品的sku是一对多的关系，通过购物车的cid可以获取多个sku
                    List<Sku> skuList = skuMapper.selectSkuListByCidAndSid(sid, cid);
                    //得到商品套餐、颜色、单个添加购物车记录商品数量
                    for (Sku sku : skuList) {
                        Integer productSeriesOnCartNum = sku.getProductNum();
                        String color = sku.getColorImage();
                        String setMeal = sku.getSetMeal();
                        String createTime = sku.getCreateTime();
                        String skuid = sku.getSkuId();
                        //从sku表中得到单个添加购物车记录的商品总价
                        Double productSeriesTotalPrice = sku.getSingleTotalPrice();
                        //数据封装：购物车总价、商品颜色、商品套餐、商品图片、商品名字、单个添加购物车记录的商品的数量、单个添加购物车记录的商品总价、单个添加购物车记录的商品id
                        cartListVo = new CartListVo();
                        cartListVo.setColor(color);
                        cartListVo.setSetMeal(setMeal);
                        cartListVo.setImage(image);
                        cartListVo.setName(name);
                        cartListVo.setSingleProductPrice(singleProductPrice);
                        cartListVo.setProductSeriesOnCartNum(productSeriesOnCartNum);
                        cartListVo.setProductSeriesTotalPrice(productSeriesTotalPrice);
                        cartListVo.setPid(pid);
                        cartListVo.setSkuid(skuid);
                        cartListVo.setCreateTime(createTime);
                        cartListVo.setCid(cid);
                        cartListVo.setAvailable(available);
                        cartResultVoList.add(cartListVo);
                    }
                }
            }
            //总价和购物车商品列表信息封装到返回信息
            CartResultVo<CartListVo> cartResultVo = new CartResultVo<>();
            cartResultVo.setCartTotalPrice(cartTotalPrice);
            cartResultVo.setCid(cid);
            //将list集合对象按照时间进行升序排序
            assert cartListVo != null;
            cartResultVoList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
            cartResultVo.setList(cartResultVoList);
            return cartResultVo;
        }
        return new CartResultVo<>();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void removeCart(Map<String, Object> map) throws Exception {
        String skuid = (String) map.get("skuid");
        String uid = (String) map.get("uid");
        String pid = (String) map.get("pid");
        //从sku中查询减少商品的数量
        Sku sku = skuMapper.selectByPrimaryKey(skuid);
        Integer decrementNumber = sku.getProductNum();
        //根据前台获取的pid查询出sid和商品单价
        Product product = productMapper.selectByPid(pid);
        String sid = product.getSeries();
        Double singlePrice = product.getNewPrice();
        //计算减少的商品总价
        Double decrementPrice = singlePrice * decrementNumber;
        //根据skuid删除点击的记录
        int skuRes = skuMapper.deleteByPrimaryKey(skuid);
        if (skuRes == 0) {
            throw new Exception(ConstantUtil.DELETE_CART_FAIL);
        }
        //根据用户id查询出购物车的cidr
        Cart cart = cartMapper.selectCartByUid(uid);
        String cid = cart.getCid();

        //修改购物车
        Map<String, Object> map1 = new HashMap<>();
        map1.put("decrementNumber", decrementNumber);
        map1.put("decrementPrice", decrementPrice);
        map1.put("updateTime", DateUtil.getDateTime());
        map1.put("cid", cid);
        int cartRes1 = cartMapper.updateByMap1(map1);
        if (cartRes1 == 0) {
            throw new Exception(ConstantUtil.DELETE_CART_FAIL);
        }
        //再次查询更新后的cart，判断购物车的product_number是否为0
        cart = cartMapper.selectCartByUid(uid);
        Integer productNumber = cart.getProductNumber();
        //根据sid和cid查询出sku表查记录序列是否长度为0
        List<Sku> skuList = skuMapper.selectSkuByCidAndSid(sid, cid);
        //如果序列长度为0，根据sid个cid删除product_series_cart_relation表
        if (skuList.size() == 0) {
            int productSeriesCartRelationRes = productSeriesCartRelationMapper.deleteBySidAndCid(sid, cid);
            if (productSeriesCartRelationRes == 0) {
                throw new Exception(ConstantUtil.DELETE_CART_FAIL);
            }
        }
        //判断购物车的product_number是否为0，如果为0就根据uid删除此用户的购物车
        if (productNumber == 0) {
            int cartRes2 = cartMapper.deleteByUid(uid);
            if (cartRes2 == 0) {
                throw new Exception(ConstantUtil.DELETE_CART_FAIL);
            }
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void editCart(Map<String, Object> map) throws Exception {
        String pid = (String) map.get("pid");
        Integer newProductNum = Integer.parseInt((String) map.get("productNum"));
        String skuid = (String) map.get("skuid");
        String uid = (String) map.get("uid");
        //通过商品的pid得到商品的单价和sid
        Product product = productMapper.selectByPid(pid);
        String sid = product.getSeries();
        Double singlePrice = product.getNewPrice();
        /**
         * 通过sid作为条件，把前端传递的商品数量在商品系列中做比较
         * 查看商品是否超卖，如果超卖值接返回信息提示，如果没超卖继续执行
         */
        ProductSeries productSeries = productSeriesMapper.selectBySidAndProductNum(sid, newProductNum);
        if (ObjectUtils.allNull(productSeries)) {
            throw new Exception(ConstantUtil.EDIT_CART_FAIL);
        }
        //根据skuid查询此商品用户原来所选择的数量，把用户新的单个商品数量减去原来的单个数量，得到此商品新增的数量
        Sku sku = skuMapper.selectByPrimaryKey(skuid);
        Integer originalProductNum = sku.getProductNum();
        Integer productNumRes = newProductNum - originalProductNum;
        Double singleTotalPriceRes = productNumRes * singlePrice;
        /**
         * 数量计算单个商品总价格
         * 根据skuid把单个单个商品总价格添加到sku里面
         */
        Double singleTotalPrice = singlePrice * newProductNum;
        sku = new Sku();
        sku.setSkuId(skuid);
        sku.setSingleTotalPrice(singleTotalPrice);
        sku.setProductNum(newProductNum);
        int skuRes = skuMapper.updateByPrimaryKeySelective(sku);
        if (skuRes == 0) {
            throw new Exception(ConstantUtil.EDIT_CART_FAIL);
        }
        //通过用户的id更新购物车的商品数量，所有商品总价，更新时间
        Cart cart = new Cart();
        cart.setProductNumber(productNumRes);
        cart.setProductTotalPrice(singleTotalPriceRes);
        cart.setUpdateTime(DateUtil.getDateTime());
        cart.setCreateBy(uid);
        int cartRes = cartMapper.updateCartByUid(cart);
        if (cartRes == 0) {
            throw new Exception(ConstantUtil.EDIT_CART_FAIL);
        }
    }
}
