package com.msg.service.impl;

import com.msg.entity.Customer;
import com.msg.mapper.CustomerMapper;
import com.msg.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by wd199 on 2018/3/20.
 */
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public Integer createCustomer(Customer record) {
        return customerMapper.insertSelective(record);
    }

    @Override
    public Integer deleteCustomer(Long custId) {
        return customerMapper.deleteByPrimaryKey(custId);
    }

    @Override
    public Integer updateCustomer(Customer record) {

        return customerMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Customer selectCustomerById(Long custId) {
        return customerMapper.selectByPrimaryKey(custId);
    }

    @Override
    public List<Customer> selectAllCustomerSelective() {
        return customerMapper.selectGroupMemberBySelective(null,null,null,null);
    }
}
