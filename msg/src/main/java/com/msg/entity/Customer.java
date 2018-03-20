package com.msg.entity;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable{
    private static final long serialVersionUID = 5716579353487548758L;
    private Long id;

    private Long custGroup;

    private Long custCode;

    private Byte custType;

    private String custName;

    private String phone;

    private Byte sex;

    private Integer source;

    private String createId;

    private Date createTime;

    private Integer valid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustGroup() {
        return custGroup;
    }

    public void setCustGroup(Long custGroup) {
        this.custGroup = custGroup;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId == null ? null : createId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }
}