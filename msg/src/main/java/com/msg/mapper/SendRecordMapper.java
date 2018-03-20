package com.msg.mapper;

import com.msg.entity.SendRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SendRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SendRecord record);

    int insertSelective(SendRecord record);

    SendRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SendRecord record);

    int updateByPrimaryKey(SendRecord record);

    /**
     * 删除短信发送记录
     * @param smsId 短信id
     * @return
     */
    int deleteBySmsId(@Param("smsId") Long smsId);

    /**
     *查询记录总数
     * @param smsId 营销短信Id
     * @return
     */
    int selectSendCountBySmsId(@Param("smsId") Long smsId);

    /**
     * 获取营销短信，发送成功失败个数
     * @param smsId 短信Id
     * @param sendStatus 发送状态
     * @return 成功、失败个数
     */
    int selectSendCountByStatus(@Param("smsId") Long smsId, @Param("sendStatus") Integer sendStatus);

    /**
     * 获取发送客户记录
     * @param smsId 短信Id
     * @return
     */
    List<SendRecord> selectSendListBySmsId(@Param("smsId") Long smsId, @Param("sendWay") Integer sendWay);

    /**
     * 通过短信的id获取发送记录条数
     * @param msgId 短信主键
     * @return
     */
    Integer selectSendNumber(@Param("msgId") int msgId);

    /**
     * 把发送短信记录的集合插入进发送短信记录表
     * @param sendRecords
     * @return
     */
    Integer insertSendRecord(List<SendRecord> sendRecords);

    /**
     * 修改重发生次数
     * @param sendRecordId
     * @return
     */
    int updateRetry(@Param("sendRecordId") Long sendRecordId, @Param("updateTime") Date updateTime, @Param("status") Integer status);

    /**
     * 查询失败手机号
     * @param mktsmsId 短信Id
     * @param sendRecordIdList 客户集合Id
     * @return 手机号集合
     */
    List<String> selectPhoneBycustCode(@Param("mktsmsId") Long mktsmsId, @Param("sendRecordIdList") List<Long> sendRecordIdList);

    /**
     * 修改短信记录发送状态
     * @param phone
     * @return
     */
    int updateSendStatus(@Param("smsId") Long smsId, @Param("phone") String phone, @Param("status") String status, @Param("retCode") String retCode);

    int updateSendRecordStatusByMegId(@Param("msgId") Long msgId, @Param("status") String status);

}