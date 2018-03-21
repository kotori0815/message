package com.msg.controller;


import com.msg.entity.User;
import com.msg.service.UserService;
import com.msg.util.Page;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wd199 on 2017/6/16.
 */
@Controller(value = "userController")
@RequestMapping("/user")
public class UserController {
    @Resource(name = "userService")
    UserService userService;

    @RequestMapping("/updateUser")
    @ResponseBody
    public int updateUser(User user) {

        return userService.modifyUser(user);
    }

    @RequestMapping("/queryUser")
    @ResponseBody
    public User queryUser(User user) {
        return userService.findUser(user);
    }

    @RequestMapping("/queryUsers")
    @ResponseBody
    public List<User> queryUsers(int rows, int page) {
        Page page1 = new Page();
        page1.setPageSize(rows);
        page1.setPageIndex(page);
        List<User> users = userService.findUsers(page1);
        int total = userService.findTotal();
        List<User> userUserDto = new ArrayList<>();
       /* userUserDto.setTotal(total);
        userUserDto.setRows(users);*/
        return userUserDto;
    }


    @RequestMapping("/queryTime")
    @ResponseBody
    public int[] queryTime() {
        int[] ints = new int[12];
        Random random = new Random();
        for (int j = 0; j < 12; j++) {
            ints[j] = random.nextInt(30);
        }
        String s = Arrays.toString(ints);
        System.err.println(s);
        return ints;
    }







}
