package com.msg.vo;

/**
 * Created by wd on 2017/10/27.
 */

import java.io.Serializable;

/**
 * 提供给程序，上传文件解析成对象的vo类
 */
public class SmsImportCustomerVO implements Serializable{


    /**
     * 客户编号
     */
    private String custCode;
    /**
     * 客户姓名
     */
    private String custName;
    /**
     * 客户类型
     */
    private String custType;
    /**
     * 客户电话
     */
    private String phone;
    /**
     * 客户email
     */
    private String email;
    /**
     * 客户性别
     */
    private String sex;
    private static final long serialVersionUID = 4917982377509966753L;
    public SmsImportCustomerVO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
