package com.msg.service;

import com.github.pagehelper.PageInfo;
import com.msg.entity.Message;

import java.util.Date;
import java.util.List;

/**
 * Created by wd on 2017/10/26.
 */
public interface SmsApproveService {
    /**
     *审批短信记录
     * @param messageId
     * @param status
     * @param reason
     * @return
     */
    int insertSmsMessageApprove(Long messageId, String status, String reason, String approveId);


    /**
     * 短信的待审批/审批通过/审批拒绝记录列表
     * @param status
     * @return
     */
    List<Message> selectSmsMessageApproveList(Integer pageStart, Integer pageSize, String status, Long messageId, Date submitStartTime, Date submitEndTime, Date approveStartTime, Date approveEndTime);

    /**
     * 审批时，根据短信id查询短信详情
     * @param messageId
     * @return
     */
    Message selectMktsmsmsMessageById(Long messageId);
}
