package com.example.service.impl;

import com.example.domain.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.util.ConstantUtil;
import com.example.util.DateUtil;
import com.example.util.MD5Util;
import com.example.util.UUIDUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @类名 UserServiceImpl
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/1 10:22
 * @版本 1.0
 */
@DubboService(interfaceClass = UserService.class, version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByMap(Map<String, Object> map) {
        map.put("password", MD5Util.getMD5((String) map.get("password")));
        return userMapper.selectUserByMap(map);
    }

    @Override
    public String queryUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    @Transactional (isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public User saveUser(Map<String, Object> map) throws Exception {
        String uid = UUIDUtil.getUUID();
        map.put("uid", uid);
        map.put("type", ConstantUtil.USER_TYPE_BUYER);
        map.put("registerTime", DateUtil.getDate());
        map.put("lastLoginTime", DateUtil.getDate());
        map.put("profile", ConstantUtil.DEFAULT_PROFILE);
        map.put("password", MD5Util.getMD5((String) map.get("password")));

        int res = userMapper.insertUser(map);
        if (res == 0) {
            throw new Exception("添加用户信息失败");
        }

        User user = userMapper.selectUserByUid(uid);
        if (ObjectUtils.allNull(user)) {
            throw new Exception("查询用户信息失败");
        }
        return user;
    }

    @Override
    public String queryUserByPhoneNumber(String phoneNumber) {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    @Override
    public User queryUserByUid(String uid) {
        return userMapper.selectUserByUid(uid);
    }

    @Override
    public int editProfile(String fileName, String uid) {
        return userMapper.updateImageByUid(fileName, uid);
    }
}
