package com.msg.service;

import com.msg.entity.Customer;
import com.msg.entity.CustomerGroup;

import java.util.List;

/**
 * Created by wd199 on 2018/3/20.
 */
public interface CustomerService {
        Integer createCustomer(Customer record);
        Integer deleteCustomer(Long custId);
        Integer updateCustomer(Customer record);
        Customer selectCustomerById(Long custId);
        List<Customer> selectAllCustomerSelective();


}
