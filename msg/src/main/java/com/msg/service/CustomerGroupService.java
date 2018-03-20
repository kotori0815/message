package com.msg.service;

import com.msg.entity.CustomerGroup;

import java.util.List;

/**
 * Created by wd199 on 2018/3/20.
 */
public interface CustomerGroupService {
    Integer createCustomerGroup(CustomerGroup record);
    Integer deleteCustomerGroup(Long custGroupId);
    Integer updateCustomerGroup(CustomerGroup record);
    CustomerGroup selectCustomerGroup(Long custGroupId);
    List<CustomerGroup> selectCustomerGroupListSelective();
}
