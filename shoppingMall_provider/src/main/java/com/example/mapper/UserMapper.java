package com.example.mapper;

import com.example.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(String uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 根据电话号码和密码查询用户
     * @param map
     * @return
     */
    User selectUserByMap(Map<String, Object> map);

    /**
     * 根据用户查找此用户名是否已经被注册
     * @param username
     * @return
     */
    String selectUserByUsername(String username);

    /**
     * 添加学生用户
     * @param map
     * @return
     */
    int insertUser(Map<String, Object> map);

    /**
     * 根据电话号码查找此号码是否已被注册
     * @param phoneNumber
     * @return
     */
    String selectUserByPhoneNumber(String phoneNumber);

    /**
     * 根据账户id查询刚注册的新用户，赋值给session
     * @param uid
     * @return
     */
    User selectUserByUid(String uid);

    /**
     *
     * @param fileName
     * @param uid
     * @return
     */
    int updateImageByUid(@Param("image") String fileName, @Param("uid") String uid);
}