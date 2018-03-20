package com.msg.mapper;


import com.msg.entity.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    /**
     * 查询客户组成员
     * @param groupId 客户组id
     * @return
     */
    List<Customer> selectGroupMemberByGroupId(@Param("groupId") Long groupId);

    /**
     * 获取客户组人员总数
     * @param groupId 客户组Id
     * @return 人员总数
     */
    int selectGroupMemberCountByGroupId(@Param("groupId") Long groupId);

    /**
     * 获取客户组成员
     * @param groupId 客户组Id
     * @return
     */
    List<Customer> selectMktsmsCustomerGroupMemberListByGroupId(@Param("groupId") Long groupId);


    List<Customer> selectGroupMemberBySelective(@Param("groupId") Long groupId,
                                                                 @Param("customerName") String customerName,
                                                                 @Param("customerNo") Long customerNo,
                                                                 @Param("customerType") String customerType);

    int insertAllMembers(List<Customer> record);

    int deleteMembers(@Param("groupId") Long groupId, @Param("customerNos") Long[] customerNos);
}