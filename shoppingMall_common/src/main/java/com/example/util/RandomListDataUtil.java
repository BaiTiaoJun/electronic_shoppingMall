package com.example.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @类名 RandomListDataUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/31 13:05
 * @版本 1.0
 */
public class RandomListDataUtil implements Serializable {

    private static final long serialVersionUID = 503292851632303913L;

    public static <T> List<T> getRandomProductsFromList(List<T> list) {
        List<T> resList = new ArrayList<>();
        List<Integer> indexList = getRandom();
        //通过下标获取随机的商品
        if (list.size() > ConstantUtil.READ_PRODUCT_NUMBER) {
            for (Integer index : indexList) {
                resList.add(list.get(index));
            }
        } else {
            resList.addAll(list);
        }
        return resList;
    }

    public static List<Integer> getRandom() {
        Random random = new Random();
        List<Integer> list = new ArrayList<>();
        do {
            int index = random.nextInt(ConstantUtil.READ_PRODUCT_NUMBER);
            if (!list.contains(index)) {
                list.add(index);
            }
        } while (list.size() < ConstantUtil.READ_PRODUCT_NUMBER);
        return list;
    }
}
