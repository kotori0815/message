package com.msg.mapper;

import com.msg.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKeyWithBLOBs(Message record);

    int updateByPrimaryKey(Message record);

    List<Message> selectMktSmsAll(@Param("smsStatus") String smsStatus, @Param("createId") String createId, @Param("smsId") Long smsId, @Param("saveTimeStart") Date saveTimeStart, @Param("saveTimeEnd") Date saveTimeEnd, @Param("submitTimeStart") Date submitTimeStart, @Param("submitTimeEnd") Date submitTimeEnd, @Param("approvalTimeStart") Date approvalTimeStart, @Param("approvalTimeEnd") Date approvalTimeEnd);

    int updateMktSmsApproveById(@Param("smsId") Long smsId, @Param("modifyId") String modifyId, @Param("modifyTime") Date modifyTime);

    List<Message> selectMessageApproveList(@Param("status") String status, @Param("messageId") Long messageId, @Param("submitStartTime") Date submitStartTime, @Param("submitEndTime") Date submitEndTime, @Param("approveStartTime") Date approveStartTime, @Param("approveEndTime") Date approveEndTime);

    int updateDataFileById(@Param("dataFile") String dataFile, @Param("smsId") Long smsId);

    int selectCountByStatus(@Param("status") String status, @Param("createId") String createId);

}