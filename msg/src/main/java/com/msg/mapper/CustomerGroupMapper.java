package com.msg.mapper;


import com.msg.entity.CustomerGroup;

public interface CustomerGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerGroup record);

    int insertSelective(CustomerGroup record);

    CustomerGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerGroup record);

    int updateByPrimaryKey(CustomerGroup record);
}