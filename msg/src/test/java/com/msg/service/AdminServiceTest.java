package com.msg.service;

import com.msg.entity.Admin;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.BaseTest;


/**
 * Created by wd on 2018/4/16.
 */
@Log4j
public class AdminServiceTest extends BaseTest {
    @Autowired
    AdminService adminService;
    @Test
    public void findAdmin() throws Exception {
        System.err.println("------测试开始-------");
        Admin admin = new Admin();
        admin.setAdminName("bigdobee");
        admin.setPassword("bigdobee");
        Admin admin1 = adminService.findAdmin(admin);
        System.err.println(admin1);
        System.err.println("------测试结束-------");
    }

    @Test
    public void addAdmin() throws Exception {
    }

}