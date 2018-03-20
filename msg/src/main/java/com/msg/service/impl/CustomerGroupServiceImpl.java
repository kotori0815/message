package com.msg.service.impl;

import com.msg.entity.CustomerGroup;
import com.msg.mapper.CustomerGroupMapper;
import com.msg.service.CustomerGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by wd199 on 2018/3/20.
 */
public class CustomerGroupServiceImpl implements CustomerGroupService {
    @Autowired
    private CustomerGroupMapper customerGroupMapper;

    @Override
    public Integer createCustomerGroup(CustomerGroup record) {
        return customerGroupMapper.insertSelective(record);
    }

    @Override
    public Integer deleteCustomerGroup(Long custGroupId) {
        return customerGroupMapper.deleteByPrimaryKey(custGroupId);
    }

    @Override
    public Integer updateCustomerGroup(CustomerGroup record) {
        return customerGroupMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public CustomerGroup selectCustomerGroup(Long custGroupId) {
        return customerGroupMapper.selectByPrimaryKey(custGroupId);
    }

    @Override
    public List<CustomerGroup> selectCustomerGroupListSelective() {
        return customerGroupMapper.SelectGroupList();
    }
}
