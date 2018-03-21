package com.msg.util;

/**
 * Created by wd on 2018/3/21.
 */

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tool {
    private static final Logger log = LoggerFactory.getLogger(Tool.class);

    private Tool() {
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static int getTextLength(String text) {
        try {
            return text != null?text.getBytes("UTF-8").length:0;
        } catch (UnsupportedEncodingException var2) {
            return text != null?text.getBytes().length:0;
        }
    }

    public static Map<String, Object> resultMap(String retStatus, String retMsg) {
        Map<String, Object> map = new HashMap();
        map.put("status", retStatus);
        if(retMsg != null) {
            map.put("message", retMsg);
        }

        return map;
    }


    public static String getSecureTel(String tel) {
        if(tel != null && tel.length() >= 7) {
            StringBuffer buff = new StringBuffer();
            buff.append(tel.substring(0, 3));
            buff.append("****");
            buff.append(tel.substring(7));
            return buff.toString();
        } else {
            return "";
        }
    }

    public static String encodeUiSn(String uiSn) {
        if(uiSn != null && uiSn.length() >= 18) {
            StringBuffer buff = new StringBuffer();
            buff.append(uiSn.substring(0, 2));
            buff.append("************");
            buff.append(uiSn.substring(14, 18));
            return buff.toString();
        } else {
            return "";
        }
    }

    public static String encodeBankNum(String bankCardNum) {
        if(bankCardNum != null && bankCardNum.length() > 4) {
            int length = bankCardNum.length();
            StringBuffer buff = new StringBuffer();
            buff.append(bankCardNum.substring(0, 4));
            if(length == 16) {
                buff.append("*******");
            } else {
                buff.append("**********");
            }

            buff.append(bankCardNum.substring(length - 4));
            return buff.toString();
        } else {
            return "";
        }
    }




    public static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap();
        Cookie[] cookies = request.getCookies();
        if(null != cookies) {
            Cookie[] var3 = cookies;
            int var4 = cookies.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Cookie cookie = var3[var5];
                cookieMap.put(cookie.getName(), cookie);
            }
        }

        return cookieMap;
    }
}