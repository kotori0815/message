package com.msg.util;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wd on 2017/11/24.
 */
public class PhoneNumUtil {
    private Logger logger = Logger.getLogger(PhoneNumUtil.class);

    /**
     * 手机号去重
     * @param users
     * @param <X>
     * @return
     */
    public static <X> List<X> getXList(List<X> users) {
        TreeSet<X> set = new TreeSet<>(new Comparator<X>() {
            @Override
            public int compare(X x1, X x2) {
                int i = 0;
                try {
                    String phone1 = String.valueOf(x1.getClass().getMethod("getPhone").invoke(x1));
                    String phone2 = String.valueOf(x2.getClass().getMethod("getPhone").invoke(x2));
                    i = phone1.compareTo(phone2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return i;
            }
        });
        set.addAll(users);
        return new ArrayList<>(set);
    }


    /**
     * 手机号去错
     * @param list
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public static List<?> removeWrongPhones(List<?> list) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Iterator<?> iterator = list.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
            Class<? extends T> aClass = (Class<? extends T>) next.getClass();
            String method="getPhone";
            Method method1 = aClass.getMethod(method);
            Object invoke = method1.invoke(next);
            String phone= (String) invoke;
            boolean matches = checkMobile(phone);
            if (!matches) {
                iterator.remove();
            }
        }
        return list;
    }

    private static boolean checkMobile(String phoneStr) {
        String[] str = phoneStr.split(",");
        Pattern p = null;
        Matcher m = null;
        for(int i=0;i<str.length;i++){
            p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
            m = p.matcher(str[i]);
            if (m.matches() == false) {
                return false;
            }
        }
        return true;
    }
}
