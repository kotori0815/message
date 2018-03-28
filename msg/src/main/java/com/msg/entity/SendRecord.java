package com.msg.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *短信的发送记录表实体类
 */
@Data
public class SendRecord implements Serializable{

    /**
     * 发送记录主键
     */
    private Long id;
    /**
     * 短信id
     */
    private Long msgId;
    /**
     * 客户编号
     */
    private Long custCode;
    /**
     * 客户类型
     */
    private Byte custType;
    /**
     * 客户姓名
     */
    private String custName;
    /**
     * 客户性别
     */
    private Byte sex;
    /**
     * 客户号码
     */
    private String phone;
    /**
     * email
     */
    private String email;
    /**
     *重发次数
     */
    private Integer retry;
    /**
     *短信记录状态
     */
    private Integer status;
    /**
     *状态码
     */
    private String retCode;
    /**
     *短信记录创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    /**
     *短信记录修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
    private static final long serialVersionUID = -5473115499010128401L;

}