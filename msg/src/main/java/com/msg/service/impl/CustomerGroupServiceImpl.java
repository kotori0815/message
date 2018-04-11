package com.msg.service.impl;

import com.msg.entity.Customer;
import com.msg.entity.CustomerGroup;
import com.msg.mapper.CustomerGroupMapper;
import com.msg.service.CustomerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wd199 on 2018/3/20.
 */
@Service("customerGroupService")
@Transactional
public class CustomerGroupServiceImpl implements CustomerGroupService {
    @Resource(name="customerGroupMapper")
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

    @Override
    public Integer insertCustomersToGroup(Long custGroupId, List<Customer> customers) {
        return null;
    }

    @Override
    public Integer deleteCustomersToGroup(Long custGroupId, List<Customer> customers) {
        return null;
    }
}
