package com.msg.controller;


import com.msg.common.CodeConts;
import com.msg.entity.Message;
import com.msg.entity.SendRecord;
import com.msg.exception.MessageSendException;
import com.msg.service.SmsSendService;
import com.msg.util.FileParse;
import com.msg.util.PhoneNumUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.Tool;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by wd on 2017/10/30.
 */
@Controller
@RequestMapping("/mktsmsSend")
@Api(description = "营销短信发送")
public class SmsSendController {
    private Logger logger = Logger.getLogger(SmsSendController.class);
    @Autowired
    private SmsSendService mktSmsSendService;


    @RequestMapping("/groupImportCreateMktsms")
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "导入客户组成员发送营销短信",consumes = "application/json")
    public Map<String, Object> mktsmsGroupCreateMktsms(HttpServletRequest request,
                                                       @ApiParam(required = true,name = "mktsmsMessage",value = "营销短信实体")@ModelAttribute Message mktsmsMessage,
                                                       @ApiParam(required = false,name = "distinct",value = "去重标识")@RequestParam(name = "distinct",required = false) Integer distinct,
                                                       @ApiParam(required = false,name = "wrong",value = "去错标识")@RequestParam(name = "wrong",required = false) Integer wrong
    ) {
        Map<String,Object> map=new HashMap<>();
        //查询当前用户信息
        //查询当前用户编码

        logger.info("--------mktsmsGroupCreate 客户组发送营销短信方式创建/发送开始----------");
//        if (mktsmsMessage == null) {
//            map.put("status",CodeConts.PARAM_LEGAL);
//            map.put("message","客户组发送营销短信方式，没有短信实体信息");
//            return map;
//        }
//        logger.info("--------客户组发送营销短信方式，短信实体为：" + mktsmsMessage + "----------");
//        if (mktsmsMessage == null) {
//            map.put("status",CodeConts.PARAM_LEGAL);
//            map.put("message","客户组发送营销短信方式，没有短信状态信息")
//            return map;
//            return Tool.resultMap(CodeConts.PARAM_LEGAL, );
//        }
//        logger.info("--------客户组发送营销短信方式，0-待提交，1-待审核，短信状态为:" + mktsmsMessage.getStatus() + "----------");
//        if ( mktsmsMessage.getCustGroup() == null) {
//            return Tool.resultMap(CodeConts.PARAM_LEGAL, "客户组发送营销短信方式，没有客户组id");
//        }
//        logger.info("--------客户组发送营销短信方式，客户组主键:" + mktsmsMessage.getCustGroup() + "----------");
//        logger.info("---------mktsmsGroupCreate 参数校验完毕-------------");
        mktsmsMessage.setSendWay(1);
//        mktsmsMessage.setCreateId(createId);
        mktsmsMessage.setCreateTime(new Date());
        if (Objects.equals(mktsmsMessage.getStatus(), "1")){
            mktsmsMessage.setModifyTime(new Date());
//            mktsmsMessage.setModifyId(createId);
        }
        try {
            map = mktSmsSendService.insertGroupCreate(mktsmsMessage);
            map.put("status","0000");
            map.put("message","成功消息");

        }catch (Exception e){
            map.put("status",CodeConts.FAILURE);
            map.put("message",e.getMessage());
            return map;
        }
        logger.info("--------mktsmsGroupCreate 客户组发送营销短信方式创建/发送结束----------");
        return map;
    }

    @RequestMapping("/uploadFileCreateMktsms")
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "导入文件方式发送营销短信",consumes = "application/json")
    public Map<String, Object> mktsmsFileCreateMktsms(HttpServletRequest request,
                                                      @ApiParam(required = true,name = "mktsmsMessage",value = "营销短信实体")@ModelAttribute Message mktsmsMessage,
                                                      @ApiParam(required = true,name = "file",value = "导入文件")@RequestParam("file") MultipartFile file) throws IOException {
        Map<String,Object> map=new HashMap<>();
        logger.info("--------mktsmsFileCreate 上传文件发送营销短信方式创建/发送开始----------");
        //参数校验
       /* if (mktsmsMessage==null){
            return Tool.resultMap(CodeConts.PARAM_LEGAL,"上传文件发送营销短信方式，没有短信实体信息");
        }
        logger.info("--------上传文件发送营销短信方式，短信实体为：" + mktsmsMessage + "----------");
        if (mktsmsMessage.getStatus()==null){
            return Tool.resultMap(CodeConts.PARAM_LEGAL,"上传文件发送营销短信方式，没有短信状态信息");
        }
        logger.info("--------上传文件发送营销短信方式，0-待提交，1-待审核，短信状态为:" +mktsmsMessage.getStatus()  + "----------");*/
        String originalFilename = file.getOriginalFilename();
        String s = originalFilename.split("\\.",(originalFilename.lastIndexOf(".")))[1];
        if (!(s.equals("xls")||s.equals("xlsx"))){
            map.put("status",CodeConts.PARAM_LEGAL);
            map.put("message","上传文件格式不正确，请上传正确的excel文件");
            return map;
        }
        //判断文件大小是否小于50M
        long fileSize = file.getSize();
        if (fileSize > 50 * 1024 * 1024) {
            logger.info("文件大小超出50M!");
            map.put("status",CodeConts.PARAM_LEGAL);
            map.put("message","录入文件大小不能超过50M!");
            logger.info("----------营销短信编辑结束----------");
            return map;
        }
        logger.info("---------mktsmsFileCreate 参数校验完毕-------------");
        int insert = 0;

        try {
            map=new HashMap<>();
//            Message mktsmsMessage1 = mktSmsSendService.setMessageId(mktsmsMessage);
            InputStream inputStream = file.getInputStream();
            List<SendRecord> list = FileParse.smsFileParse(inputStream,file.getOriginalFilename());
            int right=0;
            int unique=0;
            int size = list.size();
            if(size == 0){
                throw new MessageSendException(CodeConts.FAILURE,"客户成员不能为空");
            }
            list = (List<SendRecord>) PhoneNumUtil.removeWrongPhones(list);
            right=size-list.size();

            logger.info("添加客户组成员列表，去除重复号码用户");
            int origin=list.size();
            list =PhoneNumUtil.getXList(list);
            unique=origin-list.size();
            logger.info("--------添加客户组成员列表结束----------");
            map.put("unique",unique);
            map.put("right",right);




            mktsmsMessage.setDataFile(file.getOriginalFilename());
            //把发送记录的集合插入发送记录表的方法



            Integer integer = mktSmsSendService.insertMktsmsSendRecordList(list,mktsmsMessage.getId());
            if (integer!=list.size()){
                logger.info("--------解析条数:"+list.size()+"----------添加条数:"+integer+"--------");
//                return Tool.resultMap(CodeConts.NUMBER_NOT_EQUAL,"集合长度与发送记录的条数不符");
            }

            if (list.size()==0){
                throw new MessageSendException(CodeConts.LIST_IS_NULL,"过滤错误号码及重复号码后，客户组列表为空");
            }
            mktsmsMessage.setPhoneNum(integer);
//            mktsmsMessage.setCreateId(createrId);
            mktsmsMessage.setCreateTime(new Date());
            insert = mktSmsSendService.smsFileCreate(mktsmsMessage);
            if (insert!=1){
//                return Tool.resultMap(CodeConts.FAILURE,"上传文件发送营销短信方式，创建/发送失败");
            }
            map.put("result",insert);
            map.put("status","0000");
            if (Objects.equals(mktsmsMessage.getStatus(), "1")){
                map.put("message","上传文件发送营销短信方式,保存成功");
            }else if (Objects.equals(mktsmsMessage.getStatus(), "2")){
                map.put("message","上传文件发送营销短信方式,提交成功");
            }

        } catch (MessageSendException e) {
//            return Tool.resultMap(e.getErrorCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
//            return Tool.resultMap(CodeConts.FAILURE,e.getMessage());
        }
        logger.info("--------上传文件发送营销短信方式创建/发送结束----------");
        return map;
    }


    @RequestMapping("/downloadModel")
    @ResponseBody
    @ApiOperation(httpMethod = "GET", value = "导出模板文件")
    public void modelFileDownLoad(HttpServletResponse response,HttpServletRequest request) throws IOException {

        String path =SmsSendController.class.getClassLoader().getResource("../../").getPath();
        FileInputStream fis =new FileInputStream(new File(path+"/template/营销短信客户清单模板.xls"));
        BufferedInputStream inputStream = new BufferedInputStream(fis);
        String fileName = "营销短信客户清单模板.xls";
        if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("gbk"), "ISO8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + fileName);

        //定义输出类型
        response.setContentType("application/vnd.ms-excel");

        //定义输出类型
        response.setContentType("application/vnd.ms-excel");

        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        inputStream.close();
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(buffer);
        outputStream.flush();
        outputStream.close();
        response.flushBuffer();

    }


}
