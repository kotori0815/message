package com.msg.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msg.common.CodeConts;
import com.msg.entity.Customer;
import com.msg.entity.CustomerGroup;
import com.msg.entity.Message;
import com.msg.entity.SendRecord;
import com.msg.exception.MessageSendException;
import com.msg.mapper.CustomerGroupMapper;
import com.msg.mapper.CustomerMapper;
import com.msg.mapper.MessageMapper;
import com.msg.mapper.SendRecordMapper;
import com.msg.service.MessageService;
import com.msg.util.PhoneNumUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.*;

/**
 * 营销短信消息实现类
 *
 */
@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService {
    private Logger logger=Logger.getLogger(MessageServiceImpl.class);
    @Resource(name="messageMapper")
    private MessageMapper messageMapper;

    @Resource(name="customerGroupMapper")
    private CustomerGroupMapper customerGroupMapper;
    @Resource(name="customerMapper")
    private CustomerMapper customerMapper;
    @Resource(name="sendRecordMapper")
    private SendRecordMapper sendRecordMapper;


    /**
     * 查询营销短信
     * @param pageStart 起始页
     * @param pageSize 每页数量
     * @param smsStatus 短信状态
     * @param createId 创建人
     * @param smsId 短信Id
     * @param submitTimeStart 提交时间开始
     * @param submitTimeEnd 提交时间结束
     * @param saveTimeStart 保存时间开始
     * @param saveTimeEnd 保存时间结束
     * @return
     */
    @Override
    public List<Message> selectMktSmsAll(Integer pageStart, Integer pageSize, String smsStatus, String createId, Long smsId, Date submitTimeStart, Date submitTimeEnd, Date saveTimeStart, Date saveTimeEnd) {
        logger.info("--------获取营销短信开始----------");
        logger.info("--------营销短信，短信状态：0 待发送、1 待审核、2 审核通过、3 审核驳回----------");
        logger.info("--------营销短信，创建人："+createId+"----------"+"短信状态："+smsStatus+"----------");

        PageHelper.startPage(pageStart,pageSize,true,null,true);

        List<Message> mktsmsMessageList = messageMapper.selectMktSmsAll(smsStatus,createId,smsId,saveTimeStart,saveTimeEnd,submitTimeStart,submitTimeEnd,null,null);


        logger.info("--------获取营销短信创建人、提交人、短信签名、客户组----------");
        logger.info("--------获取营销短信结束----------");
        return mktsmsMessageList;
    }

    /**
     * 查询营销短信详情
     * @param smsId 短信id
     * @return
     */
    @Override
    public Message selectMktSmsById(Long smsId) throws MessageSendException{
        logger.info("--------获取营销短信详情开始----------");
        logger.info("--------营销短信，短信id："+smsId+"----------");

        Message mktsmsMessage=messageMapper.selectByPrimaryKey(smsId);

        if(mktsmsMessage == null){
            logger.info("--------营销短信不存在----------");
            throw new MessageSendException(CodeConts.MKTSMS_IS_NULL,"营销短信不存在");
        }
        logger.info("--------获取营销短信详情结束----------");
        return mktsmsMessage;
    }

    /**
     * 删除营销短信
     * @param smsId 短信Id
     * @return
     */
    @Override
    public int deleteMktSmsById(Long smsId) throws MessageSendException {
        logger.info("--------删除营销短信开始----------");
        logger.info("--------营销短信，短信id："+smsId+"----------");

        Message mktsmsMessage = messageMapper.selectByPrimaryKey(smsId);
        if(mktsmsMessage == null){
            logger.info("--------营销短信不存在----------");
            throw new MessageSendException(CodeConts.MKTSMS_IS_NULL,"营销短信不存在");
        }
        if(mktsmsMessage.getStatus().equals("1")){
            logger.info("--------营销短信状态为待审核，不能删除----------");
            throw new MessageSendException(CodeConts.Del_TO_APPROVE,"营销短信状态为待审核，不能删除");
        }
        if(mktsmsMessage.getStatus().equals("2")){
            logger.info("----------营销短信状态为审核通过，不能删除----------");
            throw new MessageSendException(CodeConts.Del_IS_APPROVE,"营销短信状态为审核通过，不能删除");
        }
        logger.info("--------删除营销短信发送记录开始----------");
        int num1 = sendRecordMapper.deleteBySmsId(smsId);
        logger.info("--------删除营销短信发送记录，删除了"+num1+"条----------");
        logger.info("--------删除营销短信发送记录结束----------");
        int num = messageMapper.deleteByPrimaryKey(smsId);
        logger.info("--------删除营销短信结束----------");
        return num;
    }

    /**
     * 营销短信提交审核
     * @param smsId
     * @return
     */
    @Override
    public int updateMktSmsApproveById(Long smsId, String modifyId) throws MessageSendException {
        logger.info("--------营销短信提交审核开始----------");
        logger.info("--------营销短信，短信id："+smsId+"----------");

        Message mktsmsMessage = messageMapper.selectByPrimaryKey(smsId);

        if(mktsmsMessage == null){
            logger.info("--------营销短信不存在----------");
            throw new MessageSendException(CodeConts.MKTSMS_IS_NULL,"营销短信不存在");
        }
        if(mktsmsMessage.getStatus().equals("1")){
            logger.info("--------营销短信已提交审核，不可重复提交----------");
            throw new MessageSendException(CodeConts.APPROVE_TO_APPROVE,"营销短信已提交审核，不可重复提交");
        }
        if(mktsmsMessage.getStatus().equals("2")){
            logger.info("----------营销短信已经审核通过----------");
            throw new MessageSendException(CodeConts.APPROVE_IS_SUCCESS,"营销短信已经审核通过   ");
        }
        if(mktsmsMessage.getStatus().equals("3")){
            logger.info("----------营销短信已经审核驳回----------");
            throw new MessageSendException(CodeConts.APPROVE_IS_FAILURE,"营销短信已经审核驳回");
        }
        Date date =new Date();
        int num = messageMapper.updateMktSmsApproveById(smsId,modifyId,date);

        logger.info("--------营销短信提交审核结束----------");
        return num;
    }


    /**
     * 营销短信编辑（选择）
     * @param mktsmsMessage 营销短信参数
     * @return
     */
    @Override
    public Map<String,Object> updateMktSmsChooseById(Message mktsmsMessage, boolean distinct, boolean wrong) throws MessageSendException {
        logger.info("--------营销短信（选择）编辑开始----------");
        logger.info("--------营销短信，短信id："+mktsmsMessage.getId()+"----------");
        Map<String,Object> map = new HashMap<>();
        //查询未修改信息
        Message mktsmsMessageFormer = messageMapper.selectByPrimaryKey(mktsmsMessage.getId());
        if(mktsmsMessageFormer.getStatus().equals("1")){
            logger.info("--------营销短信状态为待审核，不能编辑----------");
            throw new MessageSendException(CodeConts.APPROVE_TO_APPROVE,"营销短信状态为待审核，不能编辑");
        }
        if(mktsmsMessageFormer.getStatus().equals("2")){
            logger.info("--------营销短信状态为审核通，不能编辑----------");
            throw new MessageSendException(CodeConts.APPROVE_IS_SUCCESS,"营销短信状态为待审核，不能编辑");
        }
        //提交时间
        if(mktsmsMessage.getStatus().equals("1")){
            Date date=new Date();
            mktsmsMessage.setModifyTime(date);
        }
        int num=0;
        int dele=0;
        logger.info("--------营销短信编辑修改客户组----------");
        //修改客户组，删除营销短信记录
        dele = sendRecordMapper.deleteBySmsId(mktsmsMessage.getId());
        logger.info("--------删除发送记录，删除了："+dele+"条数据----------营销短信id："+mktsmsMessage.getId()+"----------");
        //修改营销短信记录
        map = selectGroupMemberByGroupId(mktsmsMessage.getCustGroup(),mktsmsMessage.getId(),distinct,wrong);

        int phoneNum = sendRecordMapper.selectSendCountBySmsId(mktsmsMessage.getId());
        //修改后的手机个数
        mktsmsMessage.setPhoneNum(phoneNum);

        //编辑短信
        num = messageMapper.updateByPrimaryKeySelective(mktsmsMessage);
        map.put("update",num);
        logger.info("--------营销短信（选择）编辑结束----------");
        return map;
    }

    /**
     * 营销短信编辑（录入）
     * @param mktsmsMessage 营销短信参数
     * @return
     */
    @Override
    public Map<String,Object> updateMktSmsEnteringById(Message mktsmsMessage, List<SendRecord> mktsmsSendRecordList) throws MessageSendException {
        logger.info("--------营销短信（录入）编辑开始----------");
        logger.info("--------营销短信，短信id："+mktsmsMessage.getId()+"----------");
        Map<String,Object> map = new HashMap<>();
        //获取相关短信的发送记录
        int num = sendRecordMapper.selectSendCountBySmsId(mktsmsMessage.getId());
        Message mktsmsMessage1 = messageMapper.selectByPrimaryKey(mktsmsMessage.getId());
        int num1=0;//修改数量
        try{
            //根据记录总数判断是否修改录入文件
            if(mktsmsSendRecordList != null && mktsmsSendRecordList.size()>0){
                logger.info("--------营销短信（录入）编辑修改录入文件----------");
                //修改客户组，删除营销短信记录
                int dele = sendRecordMapper.deleteBySmsId(mktsmsMessage.getId());
                logger.info("--------删除发送记录，删除了："+dele+"条数据----------营销短信id："+mktsmsMessage.getId()+"----------");
                List<SendRecord> mktsmsSendRecordListNew = new ArrayList<>();
                //文件数量大小
                int count = mktsmsSendRecordList.size();

                logger.info("添加录入文件成员，进行去错");
                mktsmsSendRecordListNew = (List<SendRecord>) PhoneNumUtil.removeWrongPhones(mktsmsSendRecordList);
                int numNew = mktsmsSendRecordListNew.size();
                int wrongNum = count - numNew;

                logger.info("添加录入文件成员，进行去重");
                mktsmsSendRecordList = PhoneNumUtil.getXList(mktsmsSendRecordListNew);
                int distinctNum = numNew - mktsmsSendRecordList.size();
                int count1 = mktsmsSendRecordList.size();
                if(count1 == 0){
                    throw new MessageSendException(CodeConts.FAILURE,"过滤错误号码及重复号码后，客户成员为空");
                }
                logger.info("--------营销短信编辑（录入）录入文件一个解析了"+count+"条记录----------");
                if(count == 0){
                    logger.info("--------营销短信编辑（录入）录入文件数据为空----------");
                    throw new MessageSendException(CodeConts.DATA_IS_NUll,"营销短信编辑（录入）录入文件数据为空");
                }
                int insertCount = 0;//添加总数量
                //每次数量
                int perCount=CodeConts.QUERY_PAGE_SIZE;
                //第几轮
                int index = 0;
                //需要几轮
                int times=mktsmsSendRecordList.size()/perCount;
                //剩余集合数量
                int excelNum = mktsmsSendRecordList.size();
                do{
                    if(excelNum == 0){
                        break;
                    }
                    List<SendRecord> sendRecords = null;

                    if(excelNum>=perCount){
                        sendRecords=mktsmsSendRecordList.subList(0, perCount);
                    }else{
                        sendRecords=mktsmsSendRecordList.subList(0, excelNum);
                    }
                    int insertNum = 0;// 每一轮添加的数量

                    for(int i = 0; i<sendRecords.size();i++){
                        SendRecord mktsmsSendRecord=mktsmsSendRecordList.get(i);
                        mktsmsSendRecord.setMsgId(mktsmsMessage.getId());//短信主键
                        Date date=new Date();
                        mktsmsSendRecord.setCreateTime(date);//创建时间
                    }

                    insertNum += sendRecordMapper.insertSendRecord(sendRecords);

                    logger.info("--------营销短信编辑（录入）添加发送记录第"+index+"轮，添加了"+insertNum+"条数据----------");
                    if(insertNum != sendRecords.size()){
                        logger.info("--------营销短信编辑（录入）添加发送记录第"+index+"轮，数据不符----------");
                    }else{
                        logger.info("--------营销短信编辑（录入）添加发送记录第"+index+"轮，添加数据相符----------");
                    }
                    excelNum -=perCount;
                    excelNum=excelNum>0?excelNum:0;
                    logger.info("--------营销短信编辑（录入）当前集合剩余长度" + excelNum + "----------");

                    insertCount += insertNum;
                    index++;
                }while (index<=times);

                logger.info("--------营销短信编辑（录入）添加发送记录一共添加了"+insertCount+"条记录----------");
                logger.info("--------营销短信编辑（录入）添加发送记录结束----------");
                if(insertCount == count1){
                    logger.info("--------营销短信编辑（录入）添加发送记录与文件解析记录相符合----------");
                }else{
                    logger.info("--------营销短信编辑（录入）添加发送记录与文件解析记录数量不符----------");
                }
                //手机个数
                int phoneNum = sendRecordMapper.selectSendCountBySmsId(mktsmsMessage.getId());
                mktsmsMessage.setPhoneNum(phoneNum);

                logger.info("添加录入文件成员，去错:"+wrongNum+"条");
                logger.info("添加录入文件成员，去重:"+distinctNum+"条");
                map.put("wrongNum",wrongNum);
                map.put("distinctNum",distinctNum);
            }

            //提交时间
            if(mktsmsMessage.getStatus().equals("1")){
                Date date=new Date();
                mktsmsMessage.setModifyTime(date);
            }

            num1 = messageMapper.updateByPrimaryKeySelective(mktsmsMessage);
            map.put("update",num1);
        }catch (MessageSendException e){
            logger.error("--------营销短信（录入）编辑异常"+e.getMessage()+"----------");
            throw new MessageSendException(e.getErrorCode(),e.getMessage());
        }catch (Exception e){
            logger.error("--------营销短信（录入）编辑异常"+e.getMessage()+"----------");
            //throw new RuntimeException();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        logger.info("--------营销短信（录入）编辑结束----------");
        return map;
    }

    /**
     * 查询营销短信发送客户记录
     * @param pageStart 起始页
     * @param pageSize 每页数量
     * @param smsId 短信Id
     * @return
     */
    @Override
    public List<SendRecord> selectMktsmsSendClientListById(Integer pageStart, Integer pageSize, Long smsId) {
        logger.info("--------获取营销短信发送客户开始----------");
        logger.info("--------营销短信，短信id："+smsId+"----------");

        Message mktsmsMessage = messageMapper.selectByPrimaryKey(smsId);
        PageHelper.startPage(pageStart,pageSize,true,null,true);

        List<SendRecord> mktsmsSendRecordList = sendRecordMapper.selectSendListBySmsId(smsId,mktsmsMessage.getSendWay());
        //手机号脱敏
        for(SendRecord mktsmsSendRecord:mktsmsSendRecordList){
            String phone=mktsmsSendRecord.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
            mktsmsSendRecord.setPhone(phone);
            if(mktsmsSendRecord.getCustType()!=null){
                if(mktsmsSendRecord.getCustType()==1){//个人
                    String custName = "";
                    if(!StringUtils.isBlank(mktsmsSendRecord.getCustName())){
                        String name = StringUtils.left(mktsmsSendRecord.getCustName(), 1);
                        custName = name + "****";
                    }
                    mktsmsSendRecord.setCustName(custName);
                }else if(mktsmsSendRecord.getCustType()==0){//机构
                    String custName = "";
                    if(!StringUtils.isBlank(mktsmsSendRecord.getCustName())){
                        String name = StringUtils.left(mktsmsSendRecord.getCustName(), 6);
                        custName = name + "****";
                    }
                    mktsmsSendRecord.setCustName(custName);
                }
            }
        }
        logger.info("--------获取营销短信发送客户结束----------");
        return mktsmsSendRecordList;
    }

    /**
     * 获取客户组成员
     * @param pageStart 起始页
     * @param pageSize 每页数量
     * @param groupId 客户组id
     * @return
     */
    @Override
    public List<Customer> selectMktsmsCustomerGroupMemberListByGroupId(Integer pageStart, Integer pageSize, Long groupId) {
        logger.info("--------获取客户组成员，客户组Id:"+groupId+"----------");

        PageHelper.startPage(pageStart,pageSize,true,null,true);

        List<Customer> mktsmsCustomerGroupMemberList = customerMapper.selectMktsmsCustomerGroupMemberListByGroupId(groupId);

        logger.info("--------获取客户组成员，手机号脱敏----------");
        //手机号脱敏
        for(Customer mktsmsCustomerGroupMember:mktsmsCustomerGroupMemberList){

            String phone=mktsmsCustomerGroupMember.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
            mktsmsCustomerGroupMember.setPhone(phone);
            if(mktsmsCustomerGroupMember.getCustType() != null){
                if(mktsmsCustomerGroupMember.getCustType()==1){//个人
                    String custName = "";
                    if(!StringUtils.isBlank(mktsmsCustomerGroupMember.getCustName())){
                        String name = StringUtils.left(mktsmsCustomerGroupMember.getCustName(), 1);
                        custName = name + "****";
                    }
                    mktsmsCustomerGroupMember.setCustName(custName);
                }else if(mktsmsCustomerGroupMember.getCustType()==0){//机构
                    String custName = "";
                    if(!StringUtils.isBlank(mktsmsCustomerGroupMember.getCustName())){
                        String name = StringUtils.left(mktsmsCustomerGroupMember.getCustName(), 6);
                        custName = name + "****";
                    }
                    mktsmsCustomerGroupMember.setCustName(custName);
                }
            }
        }
        logger.info("--------获取客户组成员结束----------");
        return mktsmsCustomerGroupMemberList;
    }

    /**
     * 获取客户组
     * @return
     */
    @Override
    public List<CustomerGroup> selectMktsmsCustomerGroupList() {
        return customerGroupMapper.SelectGroupList();
    }


    /**
     * 删除营销短信发送记录
     * @param smsId 营销短信id
     * @return
     */
    @Override
    public int deleteMktsmsSendRecordByMktsmsId(Long smsId) throws MessageSendException {
        logger.info("--------删除营销短信发送记录开始----------");
        logger.info("--------营销短信，短信id："+smsId+"----------");

        Message mktsmsMessage = messageMapper.selectByPrimaryKey(smsId);

        if(mktsmsMessage == null){
            logger.info("--------营销短信不存在----------");
            throw new MessageSendException(CodeConts.MKTSMS_IS_NULL,"营销短信不存在");
        }
        if(mktsmsMessage.getStatus().equals("1")){
            logger.info("--------营销短信状态为待审核，不能删除----------");
            throw new MessageSendException(CodeConts.Del_TO_APPROVE,"营销短信状态为待审核，不能删除");
        }
        if(mktsmsMessage.getStatus().equals("2")){
            logger.info("----------营销短信状态为待审核，不能删除----------");
            throw new MessageSendException(CodeConts.Del_IS_APPROVE,"营销短信状态为审核通过，不能删除");
        }
        int count = sendRecordMapper.selectSendCountBySmsId(smsId);
        if(count == 0){
            logger.info("----------营销短信发送记录为空，营销短信id:"+smsId+"----------");
            throw new MessageSendException(CodeConts.DATA_IS_NUll,"营销短信发送记录为空，不能删除");
        }
        int num=0;
        try {

            num = sendRecordMapper.deleteBySmsId(smsId);

            //判断短信发送方式
            if(mktsmsMessage.getSendWay() == 2){
                //录入，修改数据文件为空
                logger.info("--------删除营销短信发送方式为：2 （录入）----------");
                int num2 =messageMapper.updateDataFileById("",smsId);
                logger.info("--------删除营销短信发送记录，修改了"+num2+"条数据----------");
            }

        }catch (Exception e){
            logger.error("--------删除营销短信发送记录异常："+e.getMessage()+"----------");
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }


        logger.info("--------删除营销短信发送记录，删除了"+num+"条数据----------");
        logger.info("--------删除营销短信发送记录结束----------");
        return num;

    }




    /**
     *营销短信编辑，查询客户组成员
     * @param groupId 客户组Id
     * @param smsId 营销短信Id
     * @return
     */
    public Map<String,Object> selectGroupMemberByGroupId(Long groupId, Long smsId, boolean distinct, boolean wrong) throws MessageSendException {
        logger.info("--------营销短信编辑查询客户组人员，客户组id："+groupId+"，----------营销短信id："+smsId+"----------");
        Map<String,Object> map = new HashMap<>();
        //获取客户组总人数
        int totalNum = customerMapper.selectGroupMemberCountByGroupId(groupId);
        logger.info("--------营销短信编辑查询客户组人员，一共有："+totalNum+"条记录----------");

        int wrongNum = 0;//错误数
        int distinctNum = 0;//去重数
        int getTotal = 0;

        try{
            List<Customer> mktsmsCustomerGroupMemberList = customerMapper.selectGroupMemberByGroupId(groupId);

            int Customers = mktsmsCustomerGroupMemberList.size();
            if(Customers == 0){
                throw new MessageSendException(CodeConts.FAILURE,"客户组成员不能为空");
            }
            List<Customer> mktsmsCustomerGroupMemberNews = new ArrayList<>();

            logger.info("添加客户组成员列表，进行去错");
            mktsmsCustomerGroupMemberNews = (List<Customer>) PhoneNumUtil.removeWrongPhones(mktsmsCustomerGroupMemberList);
            int num = mktsmsCustomerGroupMemberNews.size();
            wrongNum = Customers - num;

            logger.info("添加客户组成员列表，进行去重");
            mktsmsCustomerGroupMemberList = PhoneNumUtil.getXList(mktsmsCustomerGroupMemberNews);
            distinctNum = num - mktsmsCustomerGroupMemberList.size();

            getTotal = mktsmsCustomerGroupMemberList.size();
            if(getTotal == 0){
                throw new MessageSendException(CodeConts.FAILURE,"过滤错误号码及重复号码后，客户组列表为空");
            }
            int num3 = insertMktsmsSendRecord(mktsmsCustomerGroupMemberList,smsId);

            if(getTotal != num3){
                logger.info("--------营销短信编辑查询客户组人员，添加数量与实际数量不符----------");
            }
        }catch (MessageSendException e){
            logger.error("--------营销短信（录入）编辑异常"+e.getMessage()+"----------");
            throw new MessageSendException(e.getErrorCode(),e.getMessage());
        }catch (Exception e){
            logger.error("--------营销短信编辑查询客户组人员异常："+e.getMessage()+"----------");
            e.printStackTrace();
        }
        totalNum = totalNum - wrongNum - distinctNum;
        if(getTotal == totalNum){
            logger.info("--------营销短信编辑查询客户组人员，查询数据和实际数据相符合----------");
        }else{
            logger.info("--------营销短信编辑查询客户组人员，查询数据和实际数据不符----------");

        }
        logger.info("添加客户组成员列表，去错:"+wrongNum+"条");
        logger.info("添加客户组成员列表，去重:"+distinctNum+"条");
        map.put("wrongNum",wrongNum);
        map.put("distinctNum",distinctNum);

        return map;
    }

    /**
     * 获取短信数量
     * @param status 短信状态
     * @param createId 创建人Id
     * @return
     */
    @Override
    public int selectCountByStatus(String status, String createId) {
        return messageMapper.selectCountByStatus(status,createId);
    }

    /**
     * 添加发送记录（选择）
     * @param list 客户组成员集合
     * @param smsId 营销短信id
     * @return
     */
    public int insertMktsmsSendRecord(List<Customer> list, Long smsId){
        logger.info("--------营销短信编辑添加发送记录，营销短信id："+smsId+"----------");
        int insertCount = 0;//添加总数量
        try{
            logger.info("--------营销短信编辑添加发送记录开始----------");
            //每次数量
            int perCount=CodeConts.QUERY_PAGE_SIZE;
            //第几轮
            int index = 0;
            //需要几轮
            int times=list.size()/perCount;
            //剩余集合数量
            int excelNum = list.size();
            do{
                if(excelNum == 0){
                    break;
                }
                List<Customer> mktsmsSendRecords = null;

                if(excelNum>=perCount){
                    mktsmsSendRecords=list.subList(0, perCount);
                }else{
                    mktsmsSendRecords=list.subList(0, excelNum);
                }
                int insertNum = 0;// 每一轮添加的数量
                List<SendRecord> sendRecords =new ArrayList<SendRecord>();

                for(Customer mktsmsCustomerGroupMember:mktsmsSendRecords){
                    SendRecord mktsmsSendRecord = new SendRecord();
//                    Long id= SqlSeqUtil.get("mktsms_send_record");
//                    mktsmsSendRecord.setId(id);//主键
                    mktsmsSendRecord.setMsgId(smsId);//短信主键
                    mktsmsSendRecord.setCustCode(mktsmsCustomerGroupMember.getCustCode());//客户编码
                    mktsmsSendRecord.setCustName(mktsmsCustomerGroupMember.getCustName());//客户名称
                    mktsmsSendRecord.setCustType(mktsmsCustomerGroupMember.getCustType());//客户类型
                    mktsmsSendRecord.setPhone(mktsmsCustomerGroupMember.getPhone());//手机号
                    mktsmsSendRecord.setSex(mktsmsCustomerGroupMember.getSex());//性别
                    sendRecords.add(mktsmsSendRecord);
                }

                insertNum += sendRecordMapper.insertSendRecord(sendRecords);

                logger.info("--------营销短信编辑（选择）添加发送记录第"+index+"轮，添加了"+insertNum+"条数据----------");
                if(insertNum != mktsmsSendRecords.size()){
                    logger.info("--------营销短信编辑（选择）添加发送记录第"+index+"轮，数据不符----------");
                }else{
                    logger.info("--------营销短信编辑（选择）添加发送记录第"+index+"轮，添加数据相符----------");
                }
                excelNum -=perCount;
                excelNum=excelNum>0?excelNum:0;
                logger.info("--------营销短信编辑（选择）当前集合剩余长度" + excelNum + "----------");

                insertCount += insertNum;
                index++;
                list.subList(0, mktsmsSendRecords.size()).clear();
            }while (index<=times);

            logger.info("--------营销短信编辑添加发送记录添加了"+insertCount+"条记录----------");

            logger.info("--------营销短信编辑添加发送记录结束----------");
        }catch (Exception e){
            logger.error("--------营销短信编辑添加发送记录异常："+e.getMessage()+"----------");
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException();
        }
        return insertCount;
    }


}
