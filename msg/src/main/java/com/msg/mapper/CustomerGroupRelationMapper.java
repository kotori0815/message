package com.msg.mapper;


import com.msg.entity.CustomerGroupRelation;

public interface CustomerGroupRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerGroupRelation record);

    int insertSelective(CustomerGroupRelation record);

    CustomerGroupRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerGroupRelation record);

    int updateByPrimaryKey(CustomerGroupRelation record);
}