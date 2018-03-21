package com.msg.controller;



import cn.dsna.util.images.ValidateCode;
import com.msg.entity.Admin;
import com.msg.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by wd199 on 2017/6/12.
 */
@Controller("adminController")
@RequestMapping("/admin")
public class AdminController {
    @Resource(name = "adminService")
    AdminService adminService;

    @RequestMapping("/login")
    @ResponseBody
    public String login(boolean sign, HttpSession session, String vcode, Admin admin, HttpServletResponse response) {
        System.err.println(sign);
        String msg="";
        String code = (String) session.getAttribute("code");
        try {
            if (code.equalsIgnoreCase(vcode)) {
                session.setAttribute("code", "");
                Admin admin1 = adminService.findAdmin(admin);
                if (admin1 != null) {
                    session.setAttribute("admin", admin1);
                    String username = admin1.getAdminName();
                    if (sign==true) {
                        String encode = URLEncoder.encode(username, "utf-8");
                        Cookie cookie = new Cookie("username", encode);
                        cookie.setPath("/");
                        cookie.setMaxAge(60 * 60 * 24 * 30);
                        response.addCookie(cookie);
                    }
                    msg="true";
                    return msg;
                } else {
                    throw new RuntimeException("用户名或密码不正确！请重试");
                }
            } else {
                throw new RuntimeException("验证码不正确！请重试");
            }
        } catch (Exception e) {
            msg=e.getMessage();
        }
        return msg;
    }

    @RequestMapping("/validate")
    public void validate(HttpServletResponse response, HttpSession session) throws IOException {
        ValidateCode validateCode = new ValidateCode(200, 40, 4, 10);
        String code = validateCode.getCode().toLowerCase();
        session.setAttribute("code", code);
        validateCode.write(response.getOutputStream());
    }



}
