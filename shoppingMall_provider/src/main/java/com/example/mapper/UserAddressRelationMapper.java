package com.example.mapper;

import com.example.domain.UserAddressRelation;

public interface UserAddressRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserAddressRelation record);

    int insertSelective(UserAddressRelation record);

    UserAddressRelation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserAddressRelation record);

    int updateByPrimaryKey(UserAddressRelation record);
}