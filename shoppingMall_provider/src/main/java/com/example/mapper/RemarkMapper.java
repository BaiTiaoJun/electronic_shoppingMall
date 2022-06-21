package com.example.mapper;

import com.example.domain.Remark;

import java.util.List;

/**
 * @类名 RemarkMapper
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/5/30 19:50
 * @版本 1.0
 */
public interface RemarkMapper {
    /**
     * 根据id查询评论的总数
     * @return
     */
    int selectRemarkCount(String pid);

    /**
     * 根据商品id查询所有商品评论
     * @param pid
     * @return
     */
    List<Remark> selectRemarkListById(String pid);
}
