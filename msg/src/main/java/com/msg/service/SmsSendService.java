package com.msg.service;


import com.msg.entity.Customer;
import com.msg.entity.Message;
import com.msg.entity.SendRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by wd on 2017/10/25.
 */
public interface SmsSendService {
    /**
     * 客户组导入发送短信创建/发送
     * @param message 短信对象
     * @return
     */
    Map<String,Object> insertGroupCreate(Message message) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * 上传文件发送短信创建/发送
     * @param message
     * @return
     */
    Integer smsFileCreate(Message message);

    /**
     *添加客户组成员，并去除重复号码、错误号码
     * @param groupId
     * @return
     */
    Map<String,Object> smsDistinctAndRemoveWrongPhone(Long groupId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    /**
     *
     * @param members
     * @return
     */
    List<SendRecord> insertMemberToSendRecord(List<Customer> members);




    /**
     * 将发送记录集合批量插入进短信发送记录表
     * @param records
     * @return
     */
    Integer insertMktsmsSendRecordList(List<SendRecord> records, Long msgId);

    /**
     * set短信主键
     * @param message
     * @return
     */
//    Message setMessageId(Message message);


}
