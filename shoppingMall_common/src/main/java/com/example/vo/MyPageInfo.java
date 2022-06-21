package com.example.vo;

import com.example.util.ConstantUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @类名 MyPageInfo
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/14 19:33
 * @版本 1.0
 */
public class MyPageInfo<T> extends PageInfo<T> {
    //总页数
    private int pages;
    //总条数
    private long total;
    //下一页
    private int nextPage;
    //上一页
    private int prePage;
    //每次显示条数
    private static int defaultOrderTotalSize;

    public MyPageInfo(List<T> list) {
        super(list);
    }

    public static void startPage(int i, int defaultOrderTotalSize) {
        PageHelper.startPage(i, defaultOrderTotalSize);
    }

    @Override
    public int getPages() {
        return pages;
    }

    @Override
    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public int getNextPage() {
        return getPageNum() + 1;
    }

    @Override
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    @Override
    public int getPrePage() {
        return prePage;
    }

    @Override
    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public static int getDefaultOrderTotalSize() {
        return defaultOrderTotalSize;
    }

    public static void setDefaultOrderTotalSize(int defaultOrderTotalSize) {
        MyPageInfo.defaultOrderTotalSize = defaultOrderTotalSize;
    }

    public void calculatePages(long total) {
        //计算总页数
         pages = (int) total % ConstantUtil.DEFAULT_ORDER_TOTAL_SIZE;
        if (pages == 0) {
            this.pages = pages;
        } else {
            this.pages = pages + 1;
        }
    }
}
