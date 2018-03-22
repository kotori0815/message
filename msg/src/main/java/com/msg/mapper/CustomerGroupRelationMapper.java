package com.msg.mapper;


import com.msg.entity.Customer;
import com.msg.entity.CustomerGroupRelation;

import java.util.List;

public interface CustomerGroupRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerGroupRelation record);

    int insertSelective(CustomerGroupRelation record);

    CustomerGroupRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerGroupRelation record);

    int updateByPrimaryKey(CustomerGroupRelation record);

    Integer insertCustomerGroupPunch(List<CustomerGroupRelation> customerGroupRelations);

    Integer deleteCustomerGroupPunch(List<CustomerGroupRelation> customerGroupRelations);

    Integer deleteRelationByGroupId(Long geoupId);

    List<CustomerGroupRelation> selectCustomerByGroupId(Long groupId);


}