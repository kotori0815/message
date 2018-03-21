package com.msg.entity;

/**
 * Created by wd199 on 2017/6/12.
 */
public class Admin {
    private String adminId;
    private String realname;
    private String adminName;
    private String password;
    private String salt;

    public Admin() {
    }

    public Admin(String adminId, String realname, String adminName, String password, String salt) {
        this.adminId = adminId;
        this.realname = realname;
        this.adminName = adminName;
        this.password = password;
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", realname='" + realname + '\'' +
                ", adminName='" + adminName + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
