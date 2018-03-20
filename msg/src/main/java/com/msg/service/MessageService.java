package com.msg.service;

import com.github.pagehelper.PageInfo;
import com.msg.entity.*;

import com.msg.exception.MessageSendException;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 营销短信消息接口
 */
public interface MessageService {

  /*  *//**
     * 营销短信查询
     * @param pageStart 起始页
     * @param pageSize 每页数量
     * @param smsStatus 短信状态
     * @param createId 创建人
     * @param smsId 短信Id
     * @param submitTimeStart 提交时间开始
     * @param submitTimeEnd 提交时间结束
     * @param saveTimeStart 保存时间开始
     * @param saveTimeEnd 保存时间结束
     * @return 营销短信集合
     *//*
    PageInfo<MktsmsMessageVO> selectMktSmsAll(Integer pageStart, Integer pageSize, String smsStatus, String createId, Long smsId, Date submitTimeStart, Date submitTimeEnd, Date saveTimeStart, Date saveTimeEnd);

    *//**
     * 获取营销短信详情
     * @param smsId 短信id
     * @return
     *//*
    MktsmsMessageVO selectMktSmsById(Long smsId) throws MessageSendException;

    *//**
     * 删除营销短信
     * @param smsId 短信Id
     * @return
     *//*
    int deleteMktSmsById(Long smsId) throws MessageSendException;

    *//**
     *营销短信提交审核
     * @param smsId
     * @return
     *//*
    int updateMktSmsApproveById(Long smsId, String modifyId) throws MessageSendException;

    *//**
     * 营销短信编辑(选择)
     * @param mktsmsMessage 营销短信参数
     * @return
     *//*
    Map<String,Object> updateMktSmsChooseById(MktsmsMessage mktsmsMessage, boolean distinct, boolean wrong) throws MessageSendException;

    *//**
     * 营销短信编辑(录入)
     * @param mktsmsMessage 营销短信参数
     * @return
     *//*
    Map<String,Object> updateMktSmsEnteringById(MktsmsMessage mktsmsMessage, List<MktsmsSendRecord> mktsmsSendRecordList) throws MessageSendException;

    *//**
     * 营销短信发送客户列表
     * @param pageStart 起始页
     * @param pageSize 每页数量
     * @param smsId 短信Id
     * @return 发送客户集合
     *//*
    PageInfo<MktsmsSendRecord> selectMktsmsSendClientListById(Integer pageStart, Integer pageSize, Long smsId);

    *//**
     * 获取客户组成员
     * @param pageStart 起始页
     * @param pageSize 每页数量
     * @param groupId 客户组id
     * @return
     *//*
    PageInfo<MktsmsCustomerGroupMember> selectMktsmsCustomerGroupMemberListByGroupId(Integer pageStart, Integer pageSize, Long groupId);

    *//**
     * 获取客户组
     * @return 客户组集合
     *//*
    List<MktsmsCustomerGroup> selectMktsmsCustomerGroupList();

    *//**
     * 获取短信签名
     * @return 短信签名集合
     *//*
    List<MktsmsSign> selectMktsmsSignList();

    *//**
     * 删除营销短信发送记录
     * @param smsId 营销短信id
     * @return
     *//*
    int deleteMktsmsSendRecordByMktsmsId(Long smsId) throws MessageSendException;

    *//**
     * 失败短信重发
     * @param sendRecordIdList 客户Id
     * @param mktsmsId 短信Id
     * @return
     *//*
    Map<String,Integer> phoneRetrySend(List<Long> sendRecordIdList, Long mktsmsId) throws MessageSendException;


    int selectCountByStatus(String status, String createId);*/

}
