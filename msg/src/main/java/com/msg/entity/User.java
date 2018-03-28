package com.msg.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wd199 on 2017/6/16.
 */
@Data
public class User implements Serializable{
    private static final long serialVersionUID = -2592488080683844392L;
    private String userId;
   private String realname;
   private String nickName;
   private String email;
   private String mobile;
   private String password;
   private String sex;
   private String addr;
   private String img;
   private String sign;
   private String status;
   private String salt;
   private Integer count;
   @JSONField(format = "yyyy-MM-dd")
   private Date regTime;
   @JSONField(format = "yyyy-MM-dd")
   private Date lastlogTime;

}
