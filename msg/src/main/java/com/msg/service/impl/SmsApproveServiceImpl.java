package com.msg.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.msg.common.CodeConts;
import com.msg.entity.Message;
import com.msg.entity.SendRecord;
import com.msg.exception.MessageSendException;
import com.msg.mapper.MessageMapper;
import com.msg.mapper.SendRecordMapper;
import com.msg.service.SmsApproveService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by wd on 2017/10/26.
 */
@Service("smsApproveService")
@Transactional
public class SmsApproveServiceImpl implements SmsApproveService {
    private Logger logger=Logger.getLogger(SmsApproveServiceImpl.class);
    @Resource(name="sendRecordMapper")
    private SendRecordMapper sendRecordMapper;

    @Resource(name="messageMapper")
    private MessageMapper messageMapper;

//    @Autowired
//    private SmsSendService smsSendService;




    /**
     *审核短信记录
     * @param messageId
     * @param status
     * @param reason
     * @return
     */
    @Override
    public int insertSmsMessageApprove(Long messageId, String status, String reason, String approveId) {
        logger.info("--------审核营销短信开始----------");

        Message mktsmsMessage = messageMapper.selectByPrimaryKey(messageId);
        mktsmsMessage.setStatus(status);
        mktsmsMessage.setReason(reason);
        mktsmsMessage.setApprovalId(approveId);
        mktsmsMessage.setApprovalTime(new Date());


        if (Objects.equals(status, "2")){
            //将营销短信短信发送至短信系统
            int sendMessage=1;
            List<SendRecord> list = sendRecordMapper.selectSendListBySmsId(messageId,mktsmsMessage.getSendWay());
            Integer size = list.size();
            if (list.size() > 0) {
                int pointDataLimit = CodeConts.MKTSMS_MESSAGE_NUM;
                if (pointDataLimit < size) {
                    int times = size / pointDataLimit;
                    if (size%pointDataLimit!=0){
                        times=times+1;
                    }
                    for (int i = 0; i < times; i++) {
                        List<SendRecord> records=new ArrayList<>();
                        if (list.size()<pointDataLimit){
                            records = list.subList(0, list.size());
                        }else {
                            records = list.subList(0, pointDataLimit);
                        }
                        sendMessage= this.sendMktsmsmsMessage(messageId, records);
                        if (sendMessage!=0){

                            throw new MessageSendException(CodeConts.FAILURE,"营销短信发送至短信接口失败");
                        }
                        list.subList(0, records.size()).clear();
                    }
                }
                else {
                    sendMessage= this.sendMktsmsmsMessage(messageId, list);
                    if (sendMessage!=0){
                        throw new MessageSendException(CodeConts.FAILURE,"营销短信发送至短信接口失败");
                    }
                }
            }
            int total = sendRecordMapper.updateSendRecordStatusByMegId(messageId, "1");
            if (total!=size){
                throw new MessageSendException(CodeConts.FAILURE,"营销短信发送至短信接口数目出现问题");
            }
        }
        int update = messageMapper.updateByPrimaryKeySelective(mktsmsMessage);
        if (update!=1){
            throw new MessageSendException(CodeConts.FAILURE,"审核营销短信，修改审核结果失败");
        }
        logger.info("----审核营销短信结束-----");
        return 0;
    }

    /**
     * 短信的待审核/审核通过/审核拒绝记录列表
     * @param status
     * @return
     */
    @Override
    public List<Message> selectSmsMessageApproveList(Integer pageStart, Integer pageSize, String status, Long messageId, Date submitStartTime, Date submitEndTime, Date approveStartTime, Date approveEndTime) {
        logger.info("--------审核人员查看营销短信记录列表开始----------");

        PageHelper.startPage(pageStart,pageSize,true,null,true);
        List<Message> mktsmsMessages = messageMapper.selectMessageApproveList(status,messageId,submitStartTime,submitEndTime,approveStartTime,approveEndTime);

        if (mktsmsMessages==null){
            throw new MessageSendException(CodeConts.LIST_IS_NULL,"审核人员根据条件查询短信记录列表为空");
        }
        if (mktsmsMessages.size()==0){
            throw new MessageSendException(CodeConts.DATA_IS_NUll,"该列表无数据");
        }











        logger.info("--------审核人员查看营销短信记录列表结束----------");

        logger.info("--------获取营销短信结束----------");
        return mktsmsMessages;
    }

    /**
     * 审核时，根据短信id查询短信详情
     * @param messageId
     * @return
     */
    @Override
    public Message selectMktsmsmsMessageById(Long messageId) {
        logger.info("--------审核时查看营销短信信息开始----------");
        logger.info("--------查看短信主键：" + messageId + "----------");
        Message mktsmsMessage = messageMapper.selectByPrimaryKey(messageId);
        if (mktsmsMessage==null){
            throw new MessageSendException(CodeConts.MKTSMS_IS_NULL,"审核人员查看短信时，没有以此id为主键的营销短信");
        }

        logger.info("--------审核时查看营销短信信息结束----------");
        return mktsmsMessage;
    }


    /**
     * 发送短信
     * @param messageId
     * @return
     */
    private Integer sendMktsmsmsMessage(Long messageId, List<SendRecord> list){
        Message mktsmsMessage = messageMapper.selectByPrimaryKey(messageId);
        String phone="";
        StringBuffer phones=new StringBuffer();
        for (SendRecord record : list) {
            phones.append(record.getPhone()).append(",");
        }
        if (phones.charAt(phones.length()-1)==','){
             phone=phones.substring(0, phones.length() - 1);
        }else {
            phone=phones.substring(0,phones.length());
        }

//        int mktsms = smsSendService.sendSms("MKTSMS", phone, "2", messageId, false, content);
//
//        if (mktsms==1){
//            throw new MessageSendException(CodeConts.FAILURE,"短信发送登记失败：必输参数不能为空");
//        }
//        if (mktsms==2){
//            throw new MessageSendException(CodeConts.FAILURE,"手机号格式有误，发送失败");
//        }
//        if (mktsms!=0){
//            throw new MessageSendException(CodeConts.FAILURE,"发送短信失败");
//        }
        //todo
        return 1;
    }
}
