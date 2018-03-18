package com.msg.mapper;

import com.msg.entity.SendRecord;

public interface SendRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SendRecord record);

    int insertSelective(SendRecord record);

    SendRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SendRecord record);

    int updateByPrimaryKey(SendRecord record);
}