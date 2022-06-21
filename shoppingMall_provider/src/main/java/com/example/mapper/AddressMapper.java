package com.example.mapper;

import com.example.domain.Address;

public interface AddressMapper {
    int deleteByPrimaryKey(String aid);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(String aid);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);
}