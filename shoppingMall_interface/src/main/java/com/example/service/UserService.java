package com.example.service;

import com.example.domain.User;

import java.util.Map;

/**
 * @类名 UserService
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/1 10:12
 * @版本 1.0
 */
public interface UserService {
    /**
     * 验证用户是否存在
     * @param map
     * @return
     */
    User queryUserByMap(Map<String, Object> map);

    /**
     * 根据用户查找此用户名是否已经被注册
     * @param username
     * @return
     */
    String queryUserByUsername(String username);

    /**
     * 保存新用户并且查询这个额添加的用户进行返回
     * @param map
     * @return
     */
    User saveUser(Map<String, Object> map) throws Exception;

    /**
     * 根据电话号码查找此号码是否已被注册
     * @param phoneNumber
     * @return
     */
    String queryUserByPhoneNumber(String phoneNumber);

    /**
     * 根据用户id查询个人信息并返回到个人中心
     * @param uid
     * @return
     */
    User queryUserByUid(String uid);

    /**
     * 保存图片名称到数据库中
     * @param fileName
     * @return
     */
    int editProfile(String fileName, String pid);
}
