package com.example.service.impl;

import com.example.domain.*;
import com.example.mapper.*;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.util.ConstantUtil;
import com.example.util.DateUtil;
import com.example.util.SnowFlakeUtil;
import com.example.vo.MyPageInfo;
import com.example.vo.OrderListVo;
import com.example.vo.OrderResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @类名 OrderServiceImpl
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/9 22:56
 * @版本 1.0
 */
@DubboService(interfaceClass = OrderService.class, version = "1.0.0")
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductSeriesMapper productSeriesMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Override
    public OrderResultVo<OrderListVo> generateTempOrder(String[] skuid, Double totalAmount) {
        List<OrderListVo> orderListVos = new ArrayList<>();
        OrderListVo orderListVo;
        //遍历选择的商品获取他的skuid，查询出sku对应的sid、颜色、套餐、总数量、总价集合
        for (String id : skuid) {
            Sku sku = skuMapper.selectSkuBySkuId(id);
            String sid = sku.getSid();
            String color = sku.getColorImage();
            String setMeal = sku.getSetMeal();
            Integer singleProductTotalNum = sku.getProductNum();
            Double singleProductTotalPrice = sku.getSingleTotalPrice();
            //通过sid查询对应的商品信息，pid、price、name、product
            Product product = productMapper.selectBySid(sid);
            Double price = product.getNewPrice();
            String name = product.getName();
            String image = product.getImage();
            //通过cid查询product_series得到商品的品牌
            ProductSeries productSeries = productSeriesMapper.selectByPrimaryKey(sid);
            String brand = productSeries.getBrand();
            /**
             * 封装vo返回信息：
             * 商品图片名称、商品名称、订单号、颜色、套餐、
             * 品牌、订单状态、单个记录的总数、支付状态、单个记录总价
             */
            orderListVo = new OrderListVo();
            orderListVo.setCreateTime(DateUtil.getDateTime());
            orderListVo.setSingleProductTotalPrice(singleProductTotalPrice);
            orderListVo.setSingleProductTotalNum(singleProductTotalNum);
            orderListVo.setSingleProductPrice(price);
            orderListVo.setPayStatus(ConstantUtil.PAY_STATUS_UNPAID);
            orderListVo.setOrderStatus(ConstantUtil.ORDER_STATUS_UNSUBMIT);
            orderListVo.setBrand(brand);
            orderListVo.setSetMeal(setMeal);
            orderListVo.setColor(color);
            orderListVo.setName(name);
            orderListVo.setImage(image);
            orderListVo.setSkuid(id);
            orderListVos.add(orderListVo);

        }
        //list集合中的元素按照订单的创建时间降序排序
        orderListVos.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        OrderResultVo<OrderListVo> orderResultVo = new OrderResultVo<>();
        orderResultVo.setOrderList(orderListVos);
        orderResultVo.setCheckOutPrice(totalAmount);
        return orderResultVo;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void processOrderAndProduct(String outOrderNo, String uid) throws Exception {
        //查看母订单状态，如果未支付才进行更新（幂等性）
        PayOrder payOrder = payOrderMapper.selectByPrimaryKey(outOrderNo);
        if (payOrder.getPayStatus().equals(ConstantUtil.PAY_STATUS_UNPAID) || payOrder.getPayStatus().equals(ConstantUtil.PAY_STATUS_PAID_FAIL)) {
            //更新子订单状态
            Order order = new Order();
            order.setPayOrderNo(outOrderNo);
            order.setPurcharseTime(DateUtil.getDateTime());
            order.setPayStatus(ConstantUtil.PAY_STATUS_PAID_SUCCESS);
            order.setOrderStatus(ConstantUtil.ORDER_STATUS_FINISHED);
            int orderRes = orderMapper.updateByPayOrderNo(order);
            if (orderRes == 0) {
                throw new Exception(ConstantUtil.TRADE_FAIL_INFO);
            }
            //更新支付订单状态
            payOrder = new PayOrder();
            payOrder.setPayOrderNo(outOrderNo);
            payOrder.setPayStatus(ConstantUtil.PAY_STATUS_PAID_SUCCESS);
            int payOrderRes = payOrderMapper.updateByPayOrderNo(payOrder);
            if (payOrderRes == 0) {
                throw new Exception(ConstantUtil.TRADE_FAIL_INFO);
            }
            //更新商品剩余数量
            Map<String, Object> map = new HashMap<>();
            List<Order> orderList = orderMapper.selectByPayOrderNo(outOrderNo);
            for (Order order1 : orderList) {
                String sid = order1.getSid();
                ProductSeries productSeries = productSeriesMapper.selectByPrimaryKey(sid);
                Integer version = productSeries.getVersion();
                Integer num = order1.getTotalNumber();
                map.put("sid", sid);
                map.put("version", version);
                map.put("num", num);
                int res = productSeriesMapper.editProductSeriesAvailableBySeries(map);
                if (res == 0) {
                    throw new Exception(ConstantUtil.TRADE_FAIL_INFO);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public OrderResultVo<OrderListVo> generateOrderList(String uid, Integer currentPage, Integer totalSize) throws Exception {
        OrderListVo orderListVo;
        List<OrderListVo> orderListVos = new ArrayList<>();
        Double totalUnPaidPrice = 0.00;
        Order order1;
        //根据uid统计订单
        int total = orderMapper.countOrdersByUid(uid);
        //获取每页显示数量
        int pageSize = ConstantUtil.DEFAULT_ORDER_TOTAL_SIZE;
        //添加分页
        if (ObjectUtils.isEmpty(currentPage) && ObjectUtils.isEmpty(totalSize)) {
            PageHelper.startPage(1, pageSize);
        } else {
            PageHelper.startPage(currentPage, pageSize);
        }
        /**
         * 根据用户的uid查询order，得到商品创建时间、每笔订单商品总价格、
         * 每笔订单商品数量、支付状态、商品订单号、sid、颜色、套餐、订单支付编号
         */
        List<Order> orderList = orderMapper.selectOrderListByUid(uid);
        for (Order order : orderList) {
            String createTime = order.getCreateTime();
            Double singleTotalPrice = order.getTotalPrice();
            Integer singleTotalNum = order.getTotalNumber();
            String payStatus = order.getPayStatus();
            String oid = order.getOid();
            String sid = order.getSid();
            String color = order.getColor();
            String setMeal = order.getSetMeal();
            String payOrderNo = order.getPayOrderNo();
            String orderStatus = order.getOrderStatus();
            //合计所有待支付商品的总价格
            if (StringUtils.equals(payStatus, ConstantUtil.PAY_STATUS_UNPAID)) {
                totalUnPaidPrice += singleTotalPrice;
                //如果订单表中payOrderNo字段存在，就查询支付订单表中是否存在此关联字段的记录，有就就清空它的支付订单记录，母表和子表是一对多的关系
                PayOrder payOrder = payOrderMapper.selectByPrimaryKey(payOrderNo);
                if (ObjectUtils.allNotNull(payOrder)) {
                    int payOrderRes = payOrderMapper.deleteByPrimaryKey(payOrderNo);
                    if (payOrderRes == 0) {
                        throw new Exception();
                    }
                }
                //如果是待支付状态，且payOrderNo不为空，就清空子订单的关联字和母订单表记录
                if (!StringUtils.equals(payOrderNo, "")) {
                    order1 = new Order();
                    order1.setOid(oid);
                    order1.setPayOrderNo("");
                    int orderRes = orderMapper.updateByPrimaryKeySelective(order1);
                    if (orderRes == 0) {
                        throw new Exception();
                    }
                }
            }
            //通过sid查询商品系列，得到商品的品牌、剩余数量
            ProductSeries productSeries = productSeriesMapper.selectByPrimaryKey(sid);
            String brand = productSeries.getBrand();
            Integer available = productSeries.getAvaliable();
            //通过sid查询product，得到商品名称、图片，商品单价
            Product product = productMapper.selectBySid(sid);
            Double price = product.getNewPrice();
            String name = product.getName();
            String image = product.getImage();
            String pid = product.getPid();
            //封装数据
            orderListVo = new OrderListVo();
            orderListVo.setName(name);
            orderListVo.setPayStatus(payStatus);
            orderListVo.setImage(image);
            orderListVo.setCreateTime(createTime);
            orderListVo.setColor(color);
            orderListVo.setSingleProductTotalPrice(singleTotalPrice);
            orderListVo.setSingleProductTotalNum(singleTotalNum);
            orderListVo.setBrand(brand);
            orderListVo.setOid(oid);
            orderListVo.setSetMeal(setMeal);
            orderListVo.setSingleProductPrice(price);
            orderListVo.setPid(pid);
            orderListVo.setOrderStatus(orderStatus);
            orderListVo.setAvailable(available);
            orderListVos.add(orderListVo);
        }
        //根据创建日期对list进行排序
        orderListVos.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        //将orderListVos添加到PageInfo
        PageInfo<OrderListVo> pageInfo = new PageInfo<>(orderListVos);
        //设置总页数
        int pages = total % ConstantUtil.DEFAULT_ORDER_TOTAL_SIZE;
        if (pages == 0) {
            if (total <= ConstantUtil.DEFAULT_ORDER_TOTAL_SIZE) {
                pages = 1;
                pageInfo.setPages(pages);
            } else {
                pages = total / ConstantUtil.DEFAULT_ORDER_TOTAL_SIZE;
                pageInfo.setPages(pages);
            }
        } else {
            pages = total / ConstantUtil.DEFAULT_ORDER_TOTAL_SIZE + 1;
            pageInfo.setPages(pages);
        }
        //设置总条数
        pageInfo.setTotal(total);
        //设置下一页和上一页
        if (ObjectUtils.isNotEmpty(currentPage)) {
            if (currentPage == pages) {
                pageInfo.setNextPage(1);
            } else {
                pageInfo.setNextPage(currentPage + 1);
            }
            if (currentPage != 1) {
                pageInfo.setPrePage(currentPage - 1);
            } else {
                pageInfo.setPrePage(pageInfo.getPages());
            }
        }
        //设置当前页数
        if (ObjectUtils.isNotEmpty(currentPage)) {
            pageInfo.setPageNum(currentPage);
        }
        //封装数据到OrderResultVo
        OrderResultVo<OrderListVo> resultVo = new OrderResultVo<>();
        resultVo.setTotalUnPaidPrice(totalUnPaidPrice);
        resultVo.setPageInfo(pageInfo);
        return resultVo;
    }

    @Override
    public String queryOrderByPayOrderNo(String outOrderNo) {
        List<Order> orderList = orderMapper.selectByPayOrderNo(outOrderNo);
        return orderList.get(0).getCreateBy();
    }

    @Override
    public void removeOrder(String orderId) throws Exception {
        //删除oid对应的此条记录
        int res = orderMapper.deleteByPrimaryKey(orderId);
        if (res == 0) {
            throw new Exception();
        }
    }

    @Override
    public int sureOrder(String oid) {
        Order order = new Order();
        order.setOid(oid);
        order.setOrderStatus(ConstantUtil.ORDER_STATUS_RECEIVED);
        order.setReceiveTime(DateUtil.getDateTime());
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void generateOrder(String[] skuid, String uid, String payOrderNo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Order order;
        Sku sku;
        Product product;
        for (String id : skuid) {
            //通过skuid拿到与sku关联的productseries的sid，单个订单产品数量，颜色，套餐信息
            sku = skuMapper.selectByPrimaryKey(id);
            String sid = sku.getSid();
            Integer productNum = sku.getProductNum();
            String color = sku.getColorImage();
            String setMeal = sku.getSetMeal();
            //通过sid查询product拿到产品的单价和商品的pid
            product = productMapper.selectBySid(sid);
            String pid = product.getPid();
            Double price = product.getNewPrice();
            //计算得到每一笔子订单的总价
            Double singleTotalPrice = price * productNum;
            //生成每个商品对应的子订单
            order = new Order();
            order.setOid(String.valueOf(SnowFlakeUtil.getDefaultSnowFlakeId()));
            order.setCreateTime(DateUtil.getDateTime());
            order.setCreateBy(uid);
            order.setTotalNumber(productNum);
            order.setTotalPrice(singleTotalPrice);
            order.setOrderStatus(ConstantUtil.ORDER_STATUS_SUBMIT);
            order.setPayOrderNo(ConstantUtil.PAY_STATUS_UNPAID);
            order.setSid(sid);
            order.setColor(color);
            order.setSetMeal(setMeal);
            order.setPayOrderNo(payOrderNo);
            order.setPayStatus(ConstantUtil.PAY_STATUS_UNPAID);
            int res = orderMapper.insertSelective(order);
            if (res == 0) {
                throw new Exception(ConstantUtil.GENERATE_ORDER_FAIL);
            }
            //如果成功生成订单,删除此sku相关联的购物车信息
            map.put("skuid", id);
            map.put("uid", uid);
            map.put("pid", pid);
            try {
                cartService.removeCart(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


//    @Override
//    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
//    public void generateOrder(String uid, String cid, String[] pid, Double totalAmount) throws Exception {
//        String payOrderNo = null;
//        //如果订单数大于1，就合并成一个母订单
//        if (pid.length > 1) {
//            PayOrder payOrder = new PayOrder();
//            payOrderNo = String.valueOf(SnowFlakeUtil.getDefaultSnowFlakeId());
//            payOrder.setPayOrderNo(payOrderNo);
//            payOrder.setPayStatus(ConstantUtil.PAY_STATUS_UNPAID);
//            payOrder.setCreateTime(DateUtil.getDateTime());
//            payOrder.setTotalAmount(totalAmount);
//            payOrder.setPayType(ConstantUtil.ALI_PAY);
//            int res = payOrderMapper.insertSelective(payOrder);
//            if (res == 0) {
//                throw new Exception(ConstantUtil.ORDER_STATUS_SUBMIT_FAIL);
//            }
//        }
//        //遍历商品id，查询出对应的商品系列编号sid
//        for (String id : pid) {
//            Product product = productMapper.selectSingleProductByPid(id);
//            //获取pid查询sid
//            String sid = product.getSeries();
//            //通过sid和cid得到sku表，获取商品sku属性，颜色、套餐、每条记录的总数量、单个商品总价集合
//            List<Sku> skuList = skuMapper.selectSkuListByCidAndSid(sid, cid);
//            for (Sku sku : skuList) {
//                String skuId = sku.getSkuId();
//                String color = sku.getColorImage();
//                String setMeal = sku.getSetMeal();
//                Integer productNum = sku.getProductNum();
//                Double singleProductTotalPrice = sku.getSingleTotalPrice();
//                totalAmount += singleProductTotalPrice;
//                //创建订单信息列表
//                Order order = new Order();
//                String oid = String.valueOf(SnowFlakeUtil.getDefaultSnowFlakeId());
//                String createTime = DateUtil.getDateTime();
//                order.setOid(oid);
//                order.setCreateTime(createTime);
//                order.setCreateBy(uid);
//                order.setTotalNumber(productNum);
//                order.setTotalPrice(singleProductTotalPrice);
//                order.setOrderStatus(ConstantUtil.ORDER_STATUS_UNSUBMIT);
//                order.setPayStatus(ConstantUtil.PAY_STATUS_UNPAID);
//                order.setSid(sid);
//                order.setColor(color);
//                order.setSetMeal(setMeal);
//                //如果payOrder存在，就要插入pay_order_no进行关联
//                if (ObjectUtils.allNotNull(payOrderNo)) {
//                    order.setPayOrderNo(payOrderNo);
//                }
//                int res = orderMapper.insertSelective(order);
//                if (res == 0) {
//                    throw new Exception(ConstantUtil.ORDER_STATUS_SUBMIT_FAIL);
//                }
//                //如果成功生成订单就删除此条商品的购物车相关记录
//                Map<String, Object> map = new HashMap<>();
//                map.put("skuid", skuId);
//                map.put("uid", uid);
//                map.put("pid", id);
//                try {
//                    cartService.removeCart(map);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Override
//    public OrderResultVo<OrderListVo> queryOrder(String uid) {
//        List<OrderListVo> orderListVos = new ArrayList<>();
//        OrderListVo orderListVo;
//        Double checkOutPrice = 0.00;
//        /**
//         * 根据用户的uid查询order，得到商品创建时间、
//         * 单条记录的总价、单条记录的总数、支付状态、订单状态集合、订单号、sid、颜色、套餐
//         */
//        List<Order> orders = orderMapper.selectOrderByUid(uid);
//        for (Order order : orders) {
//            String createTime = order.getCreateTime();
//            Double singleProductTotalPrice = order.getTotalPrice();
//            Integer singleProductTotalNum = order.getTotalNumber();
//            String payStatus = order.getPayStatus();
//            String orderStatus = order.getOrderStatus();
//            String oid = order.getOid();
//            String sid = order.getSid();
//            String color = order.getColor();
//            String setMeal = order.getSetMeal();
//            //查询此订单的母订单是否存在，存在就返回母订单的订单号
//
//            //计算订单的总价
//            checkOutPrice += singleProductTotalPrice;
//            //通过cid查询product_series得到商品的品牌
//            ProductSeries productSeries = productSeriesMapper.selectByPrimaryKey(sid);
//            String brand = productSeries.getBrand();
//            //通过sid查询product集合，得到商品图片名称、商品名称
//            List<Product> productList = productMapper.selectProductBySid(sid);
//            for (Product product : productList) {
//                String name = product.getName();
//                String image = product.getImage();
//                Double price = product.getNewPrice();
//                /**
//                  * 封装vo返回信息：
//                  * 商品图片名称、商品名称、订单号、颜色、套餐、
//                  * 品牌、订单状态、单个记录的总数、支付状态、订单号、单个记录总价、商户订单号
//                  */
//                orderListVo = new OrderListVo();
//                orderListVo.setCreateTime(createTime);
//                orderListVo.setSingleProductTotalPrice(singleProductTotalPrice);
//                orderListVo.setSingleProductTotalNum(singleProductTotalNum);
//                orderListVo.setSingleProductPrice(price);
//                orderListVo.setPayStatus(payStatus);
//                orderListVo.setOrderStatus(orderStatus);
//                orderListVo.setOid(oid);
//                orderListVo.setBrand(brand);
//                orderListVo.setSetMeal(setMeal);
//                orderListVo.setColor(color);
//                orderListVo.setName(name);
//                orderListVo.setImage(image);
//                orderListVos.add(orderListVo);
//            }
//        }
//        //list集合中的元素按照订单的创建时间降序排序
//        orderListVos.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
//        OrderResultVo<OrderListVo> orderResultVo = new OrderResultVo<>();
//        orderResultVo.setOrderList(orderListVos);
//        orderResultVo.setCheckOutPrice(checkOutPrice);
//        return orderResultVo;
//    }
}
