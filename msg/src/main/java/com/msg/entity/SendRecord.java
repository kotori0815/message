package com.msg.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *短信的发送记录表实体类
 */
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Long getCustCode() {
        return custCode;
    }

    public void setCustCode(Long custCode) {
        this.custCode = custCode;
    }

    public Byte getCustType() {
        return custType;
    }

    public void setCustType(Byte custType) {
        this.custType = custType;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode == null ? null : retCode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}