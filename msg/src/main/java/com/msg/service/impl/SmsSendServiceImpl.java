package com.msg.service.impl;


import com.msg.common.CodeConts;
import com.msg.entity.Customer;
import com.msg.entity.Message;
import com.msg.entity.SendRecord;
import com.msg.exception.MessageSendException;
import com.msg.mapper.CustomerMapper;
import com.msg.mapper.MessageMapper;
import com.msg.mapper.SendRecordMapper;
import com.msg.service.SmsSendService;
import com.msg.util.PhoneNumUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * Created by wd on 2017/10/26.
 */
@Service("smsSendService")
@Transactional
public class SmsSendServiceImpl implements SmsSendService {
    private Logger logger = Logger.getLogger(SmsSendServiceImpl.class);

    @Resource(name="customerMapper")
    private CustomerMapper customerMapper;

    @Resource(name="sendRecordMapper")
    private SendRecordMapper sendRecordMapper;


    @Resource(name="messageMapper")
    private MessageMapper messageMapper;


    /**
     * 客户组导入短信创建/发送
     *
     * @param mktsmsMessage 短信对象
     * @return
     */
    @Override
    public Map<String,Object> insertGroupCreate(Message mktsmsMessage) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        logger.info("--------客户组发送营销短信方式创建/发送开始----------");
        logger.info("--------客户组发送营销短信方式，短信实体为：" + mktsmsMessage + "----------");
        logger.info("--------客户组发送营销短信方式，0-待提交，1-待审核，短信状态为:" + mktsmsMessage.getStatus() + "----------");
        logger.info("--------客户组发送营销短信方式，客户组主键:" + mktsmsMessage.getCustGroup() + "----------");
//        long id = SqlSeqUtil.get("mktsms_message");
//        mktsmsMessage.setId(id);
        Map<String, Object> map = smsDistinctAndRemoveWrongPhone(mktsmsMessage.getCustGroup());
        List<Customer> mktsmsCustomerGroupMembers=null;
        List<SendRecord> records=null;
        Integer integer=null;
        if (map.containsKey("members")){
             mktsmsCustomerGroupMembers= (List<Customer>) map.get("members");
        }
        map.remove("members");
        if (mktsmsCustomerGroupMembers!=null){
            records = insertMemberToSendRecord(mktsmsCustomerGroupMembers);
        }
        if (records!=null&&records.size()>0){
            mktsmsMessage.setPhoneNum(records.size());
            integer = insertMktsmsSendRecordList(records, mktsmsMessage.getId());
        }
        int insert = messageMapper.insertSelective(mktsmsMessage);
        if (insert != 1) {
            throw new MessageSendException(CodeConts.FAILURE, "客户组发送营销短信方式，创建/发送失败");
        }
        logger.info("--------客户组发送营销短信方式创建/发送结束----------");
        map.put("insert",insert);
        if (integer!=null){
            map.put("integer",integer);
        }
        return map;
    }

    /**
     * 上传文件发送短信创建/发送
     *
     * @param mktsmsMessage
     * @return
     */
    @Override
    public Integer smsFileCreate(Message mktsmsMessage) {
        logger.info("--------上传文件发送营销短信方式创建/发送开始----------");
        logger.info("--------上传文件发送营销短信方式，短信实体为：" + mktsmsMessage + "----------");
        logger.info("--------上传文件发送营销短信方式，0-待提交，1-待审核，短信状态为:" + mktsmsMessage.getStatus() + "----------");
        //设置发送方式为上传文件发送
        if (Objects.equals(mktsmsMessage.getStatus(), "1")){
            mktsmsMessage.setModifyTime(new Date());
            mktsmsMessage.setModifyId(mktsmsMessage.getCreateId());
        }
        mktsmsMessage.setSendWay(2);
        int insert = messageMapper.insertSelective(mktsmsMessage);
        if (insert != 1) {
            throw new MessageSendException(CodeConts.FAILURE, "上传文件发送营销短信方式，创建/发送失败");
        }

        logger.info("--------上传文件发送营销短信方式创建/发送结束----------");
        return insert;
    }

    /**
     * 添加客户组成员，并去除重复号码、错误号码
     *
     * @param groupId
     * @return
     */
    @Override
    public Map<String,Object> smsDistinctAndRemoveWrongPhone(Long groupId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        logger.info("--------添加客户组成员列表开始----------");
        Map<String,Object> map=new HashMap<>();
        int unique=0;
        int right=0;
        if (groupId == null) {
            throw new MessageSendException(CodeConts.PARAM_LEGAL, "客户组id为空");
        }
        logger.info("--------添加客户组成员列表，客户组id为" + groupId + "----------");
        List<Customer> mktsmsCustomerGroupMembers = customerMapper.selectMktsmsCustomerGroupMemberListByGroupId(groupId);
        if (mktsmsCustomerGroupMembers == null) {
            throw new MessageSendException(CodeConts.MKTSMS_IS_NULL, "该id的客户组不存在");
        }
        if (mktsmsCustomerGroupMembers.size()==0){
            throw new MessageSendException(CodeConts.MKTSMS_IS_NULL, "客户组成员不能为空");
        }
            logger.info("添加客户组成员列表，去除错误号码");
        int size = mktsmsCustomerGroupMembers.size();
        mktsmsCustomerGroupMembers = (List<Customer>) PhoneNumUtil.removeWrongPhones(mktsmsCustomerGroupMembers);
        right=size-mktsmsCustomerGroupMembers.size();

        logger.info("添加客户组成员列表，去除重复号码用户");
        int origin=mktsmsCustomerGroupMembers.size();
        mktsmsCustomerGroupMembers = PhoneNumUtil.getXList(mktsmsCustomerGroupMembers);
        unique=origin-mktsmsCustomerGroupMembers.size();
        if (mktsmsCustomerGroupMembers.size()==0){
            throw new MessageSendException(CodeConts.LIST_IS_NULL,"过滤错误号码及重复号码后，客户组列表为空");
        }

        logger.info("--------添加客户组成员列表结束----------");
        map.put("unique",unique);
        map.put("right",right);
        map.put("members",mktsmsCustomerGroupMembers);
        return map;
    }

    /**
     * 将客户组成员转换为发送记录
     * @param members
     * @return
     */
    public List<SendRecord> insertMemberToSendRecord(List<Customer> members){
        logger.info("---------insertMemberToSendRecord 开始-----------");
        List<SendRecord> recordList = new ArrayList<>();
        int total=members.size();
        int sendNum = 0;
        try {
            if (members.size() > 0) {
                int pointDataLimit = CodeConts.QUERY_PAGE_SIZE;
                Integer size = members.size();
                if (pointDataLimit < size) {
                    logger.info("---------insertMemberToSendRecord 传进来的记录的list长度大于数据库单次最大转换数量"+pointDataLimit+"-----------");
                    int times = size / pointDataLimit;
                    if (size%pointDataLimit!=0){
                        times=times+1;
                    }
                    logger.info("-------------需要循环转换"+times+"次--------------");
                    for (int i = 0; i < times; i++) {
                        logger.info("--------------本次循环到第："+i+"次---------------");
                        List<Customer> list=new ArrayList<>();

                        if (members.size()<pointDataLimit){
                            list = members.subList(0, members.size());
                        }else {
                            list = members.subList(0, pointDataLimit);
                        }
                        logger.info("--------------本次循环的list长度为："+list.size()+"---------------");
                        int length=0;
                        for (Customer groupMember : list) {
                            SendRecord mktsmsSendRecord = new SendRecord();
                            mktsmsSendRecord.setCustCode(groupMember.getCustCode());
                            mktsmsSendRecord.setCustType(groupMember.getCustType());
                            mktsmsSendRecord.setCustName(groupMember.getCustName());
                            mktsmsSendRecord.setPhone(groupMember.getPhone());
                            mktsmsSendRecord.setSex(groupMember.getSex());
                            mktsmsSendRecord.setRetry(0);
                            recordList.add(mktsmsSendRecord);
                            length++;
                        }
                        logger.info("-------------本次转换成功的长度为"+length+"--------------");
                        if (length != list.size()) {
                            throw new MessageSendException(CodeConts.INSERT_RECORD_LIST_NUMBER_WRONG, "数量为2000条以上时，客户组成员转换为发送记录时数量出现错误");
                        }
                        sendNum = sendNum + list.size();
                        //
                        members.subList(0, list.size()).clear();
                    }
                }else {
                    logger.info("---------insertMemberToSendRecord 传进来的记录的list长度小于数据库单次最大转换数量"+pointDataLimit+"-----------");
                    int length= members.size();
                    logger.info("--------------本次循环的list长度为："+length+"---------------");
                    for (Customer groupMember : members) {
                        SendRecord mktsmsSendRecord = new SendRecord();
                        mktsmsSendRecord.setCustCode(groupMember.getCustCode());
                        mktsmsSendRecord.setCustType(groupMember.getCustType());
                        mktsmsSendRecord.setCustName(groupMember.getCustName());
                        mktsmsSendRecord.setPhone(groupMember.getPhone());
                        mktsmsSendRecord.setSex(groupMember.getSex());
                        mktsmsSendRecord.setRetry(0);
                        recordList.add(mktsmsSendRecord);
                    }
                    logger.info("-------------本次转换成功的长度为"+recordList.size()+"--------------");
                    if (recordList.size()!= members.size()) {

                        throw new MessageSendException(CodeConts.INSERT_RECORD_LIST_NUMBER_WRONG, "数量为2000条以下时，客户组成员转换为发送记录时数量出现错误");
                    }
                    sendNum = sendNum + members.size();
                }
            }
            if (total!=sendNum){
                throw new MessageSendException(CodeConts.INSERT_RECORD_LIST_NUMBER_WRONG, "客户组成员转换为发送记录时数量出现错误");
            }
        } catch (MessageSendException e) {
            e.printStackTrace();
            throw new MessageSendException(e.getErrorCode(),e.getMessage());
        }
        logger.info("---------insertMemberToSendRecord 结束-----------");
        return recordList;
    }



    private static ArrayList<Customer> removeDuplicateMember(List<Customer> users) {
        Set<Customer> set = new TreeSet<Customer>(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {
                //字符串,则按照asicc码升序排列
                return o1.getPhone().compareTo(o2.getPhone());
            }
        });
        set.addAll(users);
        return new ArrayList<Customer>(set);
    }


    @Override
    public Integer insertMktsmsSendRecordList(List<SendRecord> records,Long msgId) {
        logger.info("---------insertMktsmsSendRecordList 开始-----------");
        for (SendRecord record : records) {
//            record.setId(SqlSeqUtil.get("mktsms_send_record"));
//            record.setMsgId(msgId);
        }
        int total = 0;
        try {
            if (records.size() > 0) {
                int pointDataLimit = CodeConts.QUERY_PAGE_SIZE;
                Integer size = records.size();
                if (pointDataLimit < size) {
                    logger.info("---------传进来的记录的list长度大于数据库单次最大插入数量"+pointDataLimit+"-----------");
                    int times = size / pointDataLimit;
                    if (size%pointDataLimit>0){
                        times=times+1;
                    }
                    logger.info("-------------需要循环"+times+"次--------------");
                    for (int i = 0; i < times; i++) {
                        logger.info("------------本次循环到第"+i+"次---------------");
                        List<SendRecord> list=new ArrayList<>();
                        if (records.size()<pointDataLimit){
                            list = records.subList(0, records.size());
                        }else {
                            list = records.subList(0, pointDataLimit);
                        }
                        int length= list.size();
                        logger.info("--------------本次循环的list长度为："+length+"---------------");

                        Integer integer = null;
                        try {
                            integer = sendRecordMapper.insertSendRecord(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new MessageSendException(CodeConts.PARAM_LEGAL,"文件内容或格式不符");
                        }
                        logger.info("--------本次成功插入的数目为"+integer+"--------");
                        if (integer < list.size()) {

                            throw new MessageSendException(CodeConts.INSERT_RECORD_LIST_NUMBER_WRONG, "发送短信的数量出现错误");
                        }
                        total = total + list.size();
                        //
                        records.subList(0, length).clear();
                    }
                }else {
                    int length= records.size();
                    logger.info("---------传进来的记录的list长度小于数据库单次最大插入数量"+pointDataLimit+"-----------");
                    logger.info("--------------本次循环的list长度为："+length+"---------------");
                    List<SendRecord> list = records.subList(0,length);

                    Integer integer = null;
                    try {
                        integer = sendRecordMapper.insertSendRecord(list);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new MessageSendException(CodeConts.PARAM_LEGAL,"文件内容或格式不符");
                    }
                    logger.info("--------本次成功插入的数目为"+integer+"--------");
                    if (integer < list.size()) {

                        throw new MessageSendException(CodeConts.INSERT_RECORD_LIST_NUMBER_WRONG, "发送短信的数量出现错误");
                    }
                    total = total + list.size();
                    //
                    records.subList(0, length).clear();
                }
            }

        } catch (MessageSendException e) {
            e.printStackTrace();
            throw new MessageSendException(e.getErrorCode(),e.getMessage());
        }
        logger.info("---------insertMktsmsSendRecordList 结束-----------");
        return total;
    }

    /**
     * 获取主键id并set进短信实体
     * @param mktsmsMessage
     * @return
     */
    /*@Override
    public MktsmsMessage setMessageId(MktsmsMessage mktsmsMessage) {
        long messageId = SqlSeqUtil.get("mktsms_message");
        mktsmsMessage.setId(messageId);
        return mktsmsMessage;
    }*/

}
