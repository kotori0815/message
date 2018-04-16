package com.msg.mapper;

import com.msg.entity.CustomerGroup;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerGroup record);

    int insertSelective(CustomerGroup record);

    CustomerGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerGroup record);

    int updateByPrimaryKey(CustomerGroup record);

    /**
     * 获取有效客户组
     * @return
     */
    List<CustomerGroup> SelectGroupList();


    /**
     * 查询客户组表
     * @return
     */
    List<CustomerGroup> listCustomerGroupVOs(@Param("customerGroupId") Long customerGroupId,
                                                   @Param("customerGroupName") String customerGroupName,
                                                   @Param("createId") String createId,
                                                   @Param("customerGroupType") Integer customerGroupType,
                                                   @Param("status") String status);

    /**
     * 查询客户组表
     * @return
     */
   /* List<CustomerGroupVO> listCustomerGroupWithMembersCount(@Param("customerGroupId") Long customerGroupId,
                                                            @Param("customerGroupName") String customerGroupName,
                                                            @Param("createId") String createId,
                                                            @Param("customerGroupType") Integer customerGroupType,
                                                            @Param("status") String status);*/

    List<CustomerGroup> selectByGroupName(@Param("name") String name);


}