package com.example.service.impl;

import com.example.domain.*;
import com.example.mapper.*;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.util.ConstantUtil;
import com.example.util.DateUtil;
import com.example.util.RandomListDataUtil;
import com.example.util.SnowFlakeUtil;
import com.example.vo.SingleProductVo;
import com.example.vo.SplitPageRequestParamVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @类名 ProductServiceImpl
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/25 15:37
 * @版本 1.0
 */
@DubboService(interfaceClass = ProductService.class, version = "1.0.0")
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RemarkMapper remarkMapper;

    @Autowired
    private ProductColorMapper productColorMapper;

    @Autowired
    private ProductSetMealMapper productSetMealMapper;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public PageInfo<Product> queryProductList() {
        PageHelper.startPage(1, ConstantUtil.DEFAULT_HOME_TOTAL_SIZE);
        List<Product> products = productMapper.selectProductList();
        return new PageInfo<>(products);
    }

    @Override
    public PageInfo<Product> queryProductListBySplitPageAndSearch(SplitPageRequestParamVo paramVo) {
        Integer currentPage = paramVo.getCurrentPage();
        Integer totalSize = paramVo.getTotalSize();

        Map<String, Object> map = new HashMap<>();
        map.put("selectType", paramVo.getSelectType());
        map.put("searchText", paramVo.getSearchText());
        map.put("maxPrice", paramVo.getMaxPrice());
        map.put("minPrice", paramVo.getMinPrice());

        if (ObjectUtils.isEmpty(currentPage) && ObjectUtils.isEmpty(totalSize)) {
            PageHelper.startPage(1, ConstantUtil.DEFAULT_SHOP_GRID_TOTAL_SIZE);
        } else {
            PageHelper.startPage(currentPage, totalSize);
        }

        List<Product> products = productMapper.selectProductListBySplitPageSearch(map);
        return new PageInfo<>(products);
    }

    @Override
    public SingleProductVo<Product, Remark, ProductColor, ProductSetMeal> queryProductByPid(String pid) {
        Product product = productMapper.selectProductByPid(pid);
        // 评论条数
        int count = remarkMapper.selectRemarkCount(product.getPid());
        // 商品评论
        List<Remark> remarks = remarkMapper.selectRemarkListById(product.getPid());
        // 价格减少百分比
        DecimalFormat decimalFormat = new DecimalFormat("0.0%");
        String percentage = decimalFormat.format((product.getOldPrice() - product.getNewPrice()) / product.getOldPrice());
        // 随机查询四个商品展示到单个商品页面的轮播图
        List<Product> products = productMapper.selectProductListByType(product.getType());
        List<Product> res = RandomListDataUtil.getRandomProductsFromList(products);
        // 生成图片列表名
        String[] images = product.getImageTransform().split(",");
        //获取商品的sku集合
        List<ProductColor> productColors = productColorMapper.selectByPid(pid);
        List<ProductSetMeal> productSetMeals = productSetMealMapper.selectByPid(pid);
        //获取商品的图片详情列表
        String detailImageStr = product.getDetailImage();
        //获取轮播图列表
        String imageListStr = product.getImageList();
        String[] imageList = null;
        if (ObjectUtils.allNotNull(imageListStr)) {
            imageList = imageListStr.split(",");
        }
        String[] detailImages = null;
        if (ObjectUtils.allNotNull(detailImageStr) || ObjectUtils.isNotEmpty(detailImageStr)) {
            detailImages = detailImageStr.split(",");
        }
        // 封装返回的业务信息
        SingleProductVo<Product, Remark, ProductColor, ProductSetMeal> singleProductVo = new SingleProductVo<>();
        singleProductVo.setProduct(product);
        singleProductVo.setCount(count);
        singleProductVo.setPercentage(percentage);
        singleProductVo.setRemarks(remarks);
        singleProductVo.setProducts(res);
        singleProductVo.setImages(images);
        singleProductVo.setProductColorList(productColors);
        singleProductVo.setProductSetMealList(productSetMeals);
        singleProductVo.setDetailImages(detailImages);
        singleProductVo.setImageList(imageList);
        return singleProductVo;
    }

    @Override
    @Transactional
    public String createPayOrderNo(Double price, Integer num, String payment) throws Exception {
        //计算总价
        Double totalPrice = num * price;
        //生成一个pay_order，返回一个payorderno
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderNo(String.valueOf(SnowFlakeUtil.getDefaultSnowFlakeId()));
        payOrder.setPayType(payment);
        payOrder.setPayStatus(ConstantUtil.PAY_STATUS_UNPAID);
        payOrder.setCreateTime(DateUtil.getDateTime());
        payOrder.setTotalAmount(totalPrice);
        int payOrderRes = payOrderMapper.insertSelective(payOrder);
        if (payOrderRes == 0) {
            throw new Exception();
        }
        return payOrder.getPayOrderNo();
    }

    @Transactional
    @Override
    public void generateOrder(String pid, String setMeal, String color, Integer num, Double totalPrice, String uid, String payOrderNo) throws Exception {
        //添加order记录，包含sid
        Product product = productMapper.selectSingleProductByPid(pid);
        String sid = product.getSeries();
        //添加order记录，包含sid
        Order order = new Order();
        order.setOid(String.valueOf(SnowFlakeUtil.getDefaultSnowFlakeId()));
        order.setCreateTime(DateUtil.getDateTime());
        order.setPayStatus(ConstantUtil.PAY_STATUS_UNPAID);
        order.setSid(sid);
        order.setSetMeal(setMeal);
        order.setColor(color);
        order.setOrderStatus(ConstantUtil.ORDER_STATUS_SUBMIT);
        order.setTotalNumber(num);
        order.setTotalPrice(totalPrice);
        order.setPayOrderNo(payOrderNo);
        order.setCreateBy(uid);
        int orderRes = orderMapper.insertSelective(order);
        if (orderRes == 0) {
            throw new Exception();
        }
    }
}