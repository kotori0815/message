package com.msg.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
  * 对象方式接受页面参数,字符串转日期格式
  * ClassName: DateConvert.
  * User: lyc
  * Date: 2017/9/16
  * Time: 18:29
 */

public class DateConvert implements Converter<String, Date> {
    private static final Logger log = LoggerFactory.getLogger(DateConvert.class);
    @Override
    public Date convert(String stringDate) {
        if (StringUtils.isBlank(stringDate)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            log.error("", e);
        }
        return date;
    }

}
