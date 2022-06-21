package com.example.util;

/**
 * @类名 ConstaintUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/25 17:10
 * @版本 1.0
 */
public class ConstantUtil {
    public static final Integer DEFAULT_HOME_TOTAL_SIZE= 20;
    public static final String TYPE_CODE_PRODUCT_TYPE = "product_type";
    public static final Integer DEFAULT_SHOP_GRID_TOTAL_SIZE = 8;
    public static final Integer READ_PRODUCT_NUMBER = 5;
    public static final String SESSION_USER = "user";
    public static final Boolean FLAG_SUCCESS = true;
    public static final Boolean FLAG_FAIL = false;
    public static final String LOGIN_MESSAGE_FAIL = "账号或密码不正确";
    //生成图片文字的文本集合
    public static final String TEXT_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //阿里云AIP的appcode 64ac953e7e79432fb2e6a92324132eb8
    public static final String APP_CODE = "64ac953e7e79432fb2e6a92324132eb8";
    public static final String IMAGE_CODE_KEY = "imageCodeKey";
    //图片模版
    public static final String SHORT_MESSAGE_TEMPLATE = "TPL_0000";
    public static final String REGISTER_SUCCESS = "注册用户成功";
    public static final String REGISTER_FAIL = "注册用户失败";
    public static final String USER_TYPE_COMMON = "用户";
    public static final String USER_TYPE_MANAGER = "管理员";
    public static final String VERIFY_CODE_ERROR = "验证码不匹配";
    public static final String SEND_SHORT_MESSAGE_SEND = "发送短信验证码失败";
    //用户默认图片名称
    public static final String DEFAULT_PROFILE = "default-profile.jpg";
    public static final String PHONE_NUMBER_REGISTERED_TIP = "此号码已被注册";
    //集合中获商品类型的key
    public static final String PRODUCT_TYPE_KEY = "productTypeValues";
    //七牛云文件上传插件参数
    public static final String ACCESS_KEY = "MMMXkETrkyTsA0IYrWzkXR4U82OQwMZ7MRrY5QPz";
    public static final String SECRETE_KEY = "OykNdDxkgKP0IFagyrlKDpxhUFfrZF2NijJ69Ikm";
    public static final String BUCKET = "save-image-warehouse";

    public static final String UPLOAD_IMAGE_FAIL = "上传图片失败";
    //redis中存储图片名称的key
    public static final String UPLOAD_IMAGE_NAME_SET = "uploadImageNameSet";
    public static final String SAVE_IMAGE_NAME_SET = "saveImageNameSet";
    //图片保存提示信息
    public static final String SAVE_IMAGE_SUCCESS = "更改图片成功";
    public static final String SAVE_IMAGE_FAIL = "更改图片失败";
    public static final String ADD_CART_SUCCESS = "添加购物城成功";
    public static final String ADD_CART_FAIL = "添加购物车失败";
    public static final String EDIT_PRODUCT_SERIES_AVAILABLE_FAIL = "";
    public static final String ADD_SKU_FAIL = "添加商品的sku失败";
    public static final String ADD_PRODUCT_SERIES_CART_RELATION_FAIL = "添加商品系列购物车关系失败";
    //订单提交状态
    public static final String ORDER_STATUS_UNSUBMIT = "未提交";
    public static final String ORDER_STATUS_SUBMIT = "已提交";
    public static final String ORDER_STATUS_FINISHED = "已支付";
    public static final String ORDER_STATUS_RECEIVED = "已签收";
    public static final String ORDER_STATUS_SUBMIT_FAIL = "提交失败";
    public static final String ORDER_STATUS_RECEIVED_FAIl = "订单签收失败";

    public static final String DELETE_CART_FAIL = "删除购物车失败";
    public static final String EDIT_CART_FAIL = "更新购物车失败";
    public static final String PRODUCT_EXPIRE = "商品已过期";
    public static final String REMOVE_EXPIRE_PRODUCT_FAIL = "移除过期产品相关信息异常";
    public static final String PAY_STATUS_UNPAID = "待支付";
    public static final String PAY_STATUS_PAID_SUCCESS = "支付成功";
    public static final String PAY_STATUS_PAID_FAIL = "支付失败";

    //申请支付宝接口的必要参数
    public static final String ORDER_NAME = "付款";
    public static final String PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";//FAST_INSTANT_TRADE_PAY：新快捷即时到账产品
    public static final String TIMEOUT_EXPRESS = "11m";//超时时间10分钟
    public static final String CERT_TYPE = "IDENTITY_CARD";//IDENTITY_CARD：身份证
    public static final String NEED_CHECK_INFO = "T";//需要强制校验传：T

    //支付方式
    public static final String ALI_PAY = "AliPay";
    public static final String WECHAT_PAY = "WechatPay";
    public static final String UNION_PAY = "UnionPay";

    //校验通知
    public static final String VERIFY_FAIL_LOG = "校验失败";
    public static final String TRADE_FAIL = "failure";
    public static final boolean TRADE_FAIL_FLAG = false;
    public static final boolean TRADE_SUCCESS_FLAG = true;
    public static final String TRADE_SUCCESS= "TRADE_SUCCESS";
    public static final String TRADE_FAIL_INFO= "交易失败";

    //订单通知
    public static final String GENERATE_ORDER_FAIL = "生成订单失败";
    public static final String REMOVE_ORDER_FAIL = "取消订单失败";

    public static final int DEFAULT_ORDER_TOTAL_SIZE = 5;

    public static final Object USER_TYPE_BUYER = "买家";
}
