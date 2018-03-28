package com.msg.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 营销短信消息实体类
 */
@Data
public class Message implements Serializable{
    private static final long serialVersionUID = 2524973005095191470L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 短信签名
     */
    private Long sign;
    /**
     * 短信状态
     */
    private String status;
    /**
     * 发送方式
     */
    private Integer sendWay;
    /**
     * 客户组
     */
    private Long custGroup;
    /**
     *数据文件
     */
    private String dataFile;
    /**
     * 手机个数
     */
    private Integer phoneNum;
    /**
     * 说明
     */
    private String memo;
    /**
     * 创建人
     */
    private String createId;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    /**
     * 提交人
     */
    private String modifyId;
    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date modifyTime;
    /**
     * 审核人
     */
    private String approvalId;
    /**
     * 审核时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date approvalTime;
    /**
     * 驳回理由
     */
    private String reason;
    /**
     *短信内容
     */
    private String content;


}