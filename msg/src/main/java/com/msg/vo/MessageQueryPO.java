package com.msg.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 营销短信消息查询条件实体类
 *
 */
public class MessageQueryPO implements Serializable{
    private static final long serialVersionUID = 2401729404799549188L;
    /**
     * 起始页
     */
    private Integer pageStart;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 短信状态
     */
    private String smsStatus;
    /**
     * 营销短信Id
     */
    private Long smsId;
    /**
     * 提交时间开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date submitTimeStart;
    /**
     * 提交时间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date submitTimeEnd;
    /**
     * 保存时间开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date saveTimeStart;

    /**
     * 保存时间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date saveTimeEnd;

    public MessageQueryPO() {
    }

    public MessageQueryPO(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getPageStart() {
        return pageStart;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public Long getSmsId() {
        return smsId;
    }

    public void setSmsId(Long smsId) {
        this.smsId = smsId;
    }
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getSubmitTimeStart() {
        return submitTimeStart;
    }

    public void setSubmitTimeStart(Date submitTimeStart) {
        this.submitTimeStart = submitTimeStart;
    }
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getSaveTimeStart() {
        return saveTimeStart;
    }

    public void setSaveTimeStart(Date saveTimeStart) {
        this.saveTimeStart = saveTimeStart;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getSubmitTimeEnd() {
        return submitTimeEnd;
    }

    public void setSubmitTimeEnd(Date submitTimeEnd) {
        this.submitTimeEnd = submitTimeEnd;
    }




    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getSaveTimeEnd() {
        return saveTimeEnd;
    }

    public void setSaveTimeEnd(Date saveTimeEnd) {
        this.saveTimeEnd = saveTimeEnd;
    }

    @Override
    public String toString() {
        return "MktsmsMessageQueryVO{" +
                "pageStart=" + pageStart +
                ", pageSize=" + pageSize +
                ", smsStatus='" + smsStatus + '\'' +
                ", smsId=" + smsId +
                ", submitTimeStatr=" + submitTimeStart +
                ", submitTimeEnd=" + submitTimeEnd +
                ", saveTimeStatr=" + saveTimeStart +
                ", saveTimeEnd=" + saveTimeEnd +
                '}';
    }
}
