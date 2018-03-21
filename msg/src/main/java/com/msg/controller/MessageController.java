package com.msg.controller;


import com.github.pagehelper.PageInfo;

import com.msg.common.CodeConts;
import com.msg.entity.Customer;
import com.msg.entity.CustomerGroup;
import com.msg.entity.Message;
import com.msg.entity.SendRecord;
import com.msg.exception.MessageSendException;
import com.msg.service.MessageService;

import com.msg.util.FileParse;
import com.msg.util.Tool;
import com.msg.vo.MessageQueryPO;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;

/**
 * 营销短信消息Controller
 */
@Controller
@RequestMapping("/mktsmsMessage")
@Api(description = "营销短信消息")
public class MessageController {
    private Logger logger=Logger.getLogger(MessageController.class);

    @Autowired
    private MessageService mktsmsMessageService;

    /**
     * 查询营销短信
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value ="/selectMktsmsList",method= RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "查询营销短信")
    public Map<String,Object> selectMktsmsMessageList(HttpServletRequest request
                                                     // @ApiParam(name = "mktsmsMessageQueryVO", value = "营销短信查询条件")@RequestBody(required = false) MktsmsMessageQueryVO mktsmsMessageQueryVO
                                                      ){
        logger.info("---------------selectMktsmsMessageList() 获取营销短信开始--------------------------");
        Map<String,Object> map=new HashMap<String,Object>();




        Integer pageStart= 1/*mktsmsMessageQueryVO.getPageStart()*/;
        Integer pageSize=10/*mktsmsMessageQueryVO.getPageSize()*/;
        //判断起始页和页面大小是否为空
        if (pageStart==null){
            //设置默认起始页面，为1，即首页
            pageStart=1;
        }
        if (pageSize==null){
            //设置默认页面大小，为10
            pageSize=10;
        }


//        if(mktsmsMessageQueryVO.getSmsStatus().equals("")){
//            logger.info("---------------selectMktsmsMessageList() 非法参数，方法结束--------------------------");
//            return Tool.resultMap(CodeConts.PARAM_LEGAL,"营销短信状态为空");
//        }

        //判断起始页面和页面大小是否非法
        if (pageStart<1||pageSize<0){
            map.put("status",CodeConts.PARAM_LEGAL);
            map.put("message","非法请求，请重试!");
            logger.info("查询参数非法，起始页码或页面大小错误");
        }else {
            List<Message> pageInfo = null;

            try {
//                Long smsId = mktsmsMessageQueryVO.getSmsId();
//                String smsStatus = mktsmsMessageQueryVO.getSmsStatus();
//                Date submitTimeStart = mktsmsMessageQueryVO.getSubmitTimeStart();
//                Date submitTimeEnd = mktsmsMessageQueryVO.getSubmitTimeEnd();
//                Date saveTimeStart = mktsmsMessageQueryVO.getSaveTimeStart();
//                Date saveTimeEnd = mktsmsMessageQueryVO.getSaveTimeEnd();
                pageInfo = mktsmsMessageService.selectMktSmsAll(pageStart,pageSize,null,"",null,null,null,null,null);

            }catch (Exception e){
                logger.info("获取营销异常，异常信息为:"+e.getMessage());
                map = Tool.resultMap(CodeConts.FAILURE,"获取营销短信异常！");
                logger.info("-----------------selectMktsmsMessageList() 方法执行结束----------------");
                return map;
            }
            //判断结果
            if (pageInfo!=null && !(pageInfo.size()>0)) {

                map = Tool.resultMap(CodeConts.SUCCESS, "获取成功");
                map.put("page",pageInfo);
//                logger.info("一共查询出"+pageInfo.getSize()+"条记录");
            }else{
                logger.info("---------------selectMktsmsMessageList() 结果为空，方法结束--------------------------");
                map=Tool.resultMap(CodeConts.DATA_IS_NUll, "结果为空");
                return map;
            }
        }
        logger.info("---------------selectMktsmsMessageList() 获取成功，方法结束--------------------------");
        return map;
    }

    /**
     * 删除营销短信
     * @param request
     * @param mktsmsId 营销短信id
     * @return
     */
    @RequestMapping(value="deleteMktsms",method= RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "删除营销短信")
    public Map<String,Object> delectMktsmsBySmsId(HttpServletRequest request,@ApiParam(required = true, name = "mktsmsId", value = "营销短信id")@RequestParam(value = "mktsmsId")Long mktsmsId){
        logger.info("---------------delectMktsmsBySmsId() 通过营销短信id删除短信--------------------------");
        logger.info("前端传入的参数为 [营销短信id:"+mktsmsId+"]");
        try{
            Map<String,Object> map=new HashMap<String,Object>();
            //查询当前用户信息

            //参数校验
            if(mktsmsId == null){
                logger.info("---------------delectMktsmsBySmsId() 非法参数，方法结束--------------------------");
                return Tool.resultMap(CodeConts.PARAM_LEGAL, "非法参数");
            }

            int result=mktsmsMessageService.deleteMktSmsById(mktsmsId);

            if(result>0){
                logger.info("----------------------删除营销短信成功！delectMktsmsBySmsId() 方法结束-------------------");
                return Tool.resultMap(CodeConts.SUCCESS, "删除成功!");
            }else{
                logger.info("----------------------删除营销短信失败！delectMktsmsBySmsId() 方法结束-------------------");
                return Tool.resultMap(CodeConts.FAILURE, "删除失败!");
            }
        }catch (MessageSendException e){
            logger.error("delectMktsmsBySmsId()  error:", e);
            return Tool.resultMap(e.getErrorCode(), e.getLocalizedMessage());
        }
    }

    /**
     * 营销短信提交审核
     * @param request
     * @param mktsmsId 营销短信id
     * @return
     */
    @RequestMapping(value="submitApproveMktsms",method= RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "营销短信提交审核")
    public Map<String,Object> submitApproveMktsmsBysmsId(HttpServletRequest request,@ApiParam(required = true, name = "mktsmsId", value = "营销短信id")@RequestParam(value = "mktsmsId")Long mktsmsId){
        logger.info("---------------submitApproveMktsmsBysmsId() 营销短信提交审核--------------------------");
        logger.info("前端传入的参数为 [营销短信id:"+mktsmsId+"]");
        try{
            Map<String,Object> map=new HashMap<String,Object>();


            //参数校验
            if(mktsmsId == null){
                logger.info("---------------submitApproveMktsmsBysmsId() 非法参数，方法结束--------------------------");
                return Tool.resultMap(CodeConts.PARAM_LEGAL, "非法参数");
            }

            int result=mktsmsMessageService.updateMktSmsApproveById(mktsmsId,null);

            if(result>0){
                logger.info("----------------------营销短信提交审核成功！submitApproveMktsmsBysmsId() 方法结束-------------------");
                return Tool.resultMap(CodeConts.SUCCESS, "提交审核成功!");
            }else{
                logger.info("----------------------营销短信提交审核失败！submitApproveMktsmsBysmsId() 方法结束-------------------");
                return Tool.resultMap(CodeConts.FAILURE, "提交审核失败!");
            }
        }catch (MessageSendException e){
            logger.error("submitApproveMktsmsBysmsId()  error:", e);
            return Tool.resultMap(e.getErrorCode(), e.getLocalizedMessage());
        }
    }

    /**
     * 查看营销短信详情
     * @param request
     * @param mktsmsId 营销短信id
     * @return
     */
    @RequestMapping(value = "selectMktsms",method= RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "查看营销短信详情")
    public Map<String,Object> selectMktsmsByMktsmsId(HttpServletRequest request,@ApiParam(required = true, name = "mktsmsId", value = "营销短信id")@RequestParam(value = "mktsmsId")Long mktsmsId){
        logger.info("---------------selectMktsmsByMktsmsId() 查看营销短信详情--------------------------");
        logger.info("前端传入的参数为 [营销短信id:"+mktsmsId+"]");
        try{
            Map<String,Object> map=new HashMap<String,Object>();

            //参数校验
            if(mktsmsId == null){
                logger.info("---------------selectMktsmsByMktsmsId() 非法参数，方法结束--------------------------");
                return Tool.resultMap(CodeConts.PARAM_LEGAL, "非法参数");
            }

             Message mktsmsMessageVO=mktsmsMessageService.selectMktSmsById(mktsmsId);

            if(mktsmsMessageVO == null){
                logger.info("----------------------查看营销短信详情数据为空！selectMktsmsByMktsmsId() 方法结束-------------------");
                return Tool.resultMap(CodeConts.DATA_IS_NUll, "数据为空!");
            }else{
                map.put("status",CodeConts.SUCCESS);
                map.put("message","获取成功!");
                map.put("Object",mktsmsMessageVO);
                logger.info("----------------------查看营销短信详情成功！selectMktsmsByMktsmsId() 方法结束-------------------");
                return map;
            }
        }catch (MessageSendException e){
            logger.error("selectMktsmsByMktsmsId()  error:", e);
            return Tool.resultMap(e.getErrorCode(), e.getLocalizedMessage());
        }
    }

    /**
     * 获取客户组
     * @return
     */
    @RequestMapping(value = "selectMktsmsCustomerGroupList",method= RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "获取客户组")
    public Map<String,Object> selectMktsmsCustomerGroupList(){
        logger.info("---------------selectMktsmsCustomerGroupList() 获取客户组--------------------------");
        Map<String,Object> map = new HashMap<String,Object>();

        List<CustomerGroup> mktsmsCustomerGroupList = mktsmsMessageService.selectMktsmsCustomerGroupList();
        //判断结果集
        if(mktsmsCustomerGroupList.isEmpty() && mktsmsCustomerGroupList.size() == 0){
            logger.info("---------------selectMktsmsCustomerGroupList() 获取客户组,数据为空，方法结束--------------------------");
            return Tool.resultMap(CodeConts.DATA_IS_NUll, "获取失败");
        }

        map = Tool.resultMap(CodeConts.SUCCESS, "获取成功");
        map.put("list",mktsmsCustomerGroupList);
        logger.info("---------------selectMktsmsCustomerGroupList() 获取客户组成功，方法结束--------------------------");
        return map;
    }



    /**
     * 获取营销短信发送客户记录
     * @param request
     * @param mktsmsId 营销短信id
     * @return
     */
    @RequestMapping(value ="selectMktsmsSendClientList",method= RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "获取营销短信发送客户记录")
    public Map<String,Object> selectMktsmsSendClientList(HttpServletRequest request,
                                                         @ApiParam(required = true, name = "pageStart", value = "起始页")@RequestParam(value = "pageStart")Integer pageStart,
                                                         @ApiParam(required = true, name = "pageSize", value = "每页数量")@RequestParam(value = "pageSize")Integer pageSize,
                                                         @ApiParam(required = true, name = "mktsmsId", value = "营销短信id")@RequestParam(value = "mktsmsId")Long mktsmsId) {
        logger.info("---------------selectMktsmsSendClientList() 获取营销短信发送客户记录--------------------------");
        logger.info("前端传入的参数为 [营销短信id:" + mktsmsId + "]");
        Map<String, Object> map = new HashMap<String, Object>();

        //查询当前用户信息
        Cookie cookie = Tool.ReadCookieMap(request).get("token");
        if (cookie == null || StringUtil.isBlank(cookie.getValue())) {
            return Tool.resultMap(CodeConts.DATA_IS_NUll, "用户未登录");
        }

        //判断营销短信id
        if (mktsmsId == null) {
            logger.info("---------------selectMktsmsSendClientList() 非法参数，方法结束--------------------------");
            return Tool.resultMap(CodeConts.PARAM_LEGAL, "营销短信id不能为空");
        }
        //判断起始页和页面大小是否为空
        if (pageStart == null) {
            //设置默认起始页面，为1，即首页
            pageStart = 1;
        }
        if (pageSize == null) {
            //设置默认页面大小，为10
            pageSize = 10;
        }

        //判断起始页面和页面大小是否非法
        if (pageStart < 1 || pageSize < 0) {
            map.put("status", CodeConts.PARAM_LEGAL);
            map.put("message", "非法请求，请重试!");
            logger.info("查询参数非法，起始页码或页面大小错误");
        } else {
            List<SendRecord> pageInfo = null;

            try {

                pageInfo = mktsmsMessageService.selectMktsmsSendClientListById(pageStart, pageSize, mktsmsId);

            } catch (Exception e) {
                logger.info("获取营销异常，异常信息为:" + e.getMessage());
                map = Tool.resultMap(CodeConts.FAILURE, "获取营销异常");
                logger.info("-----------------selectMktsmsSendClientList() 方法执行结束----------------");
                return map;
            }
            //判断结果
            if (pageInfo != null && !(pageInfo.size() > 0)) {

                map = Tool.resultMap(CodeConts.SUCCESS, "获取成功");
                map.put("page", pageInfo);
                logger.info("一共查询出" + pageInfo.size() + "条记录");
            } else {
                logger.info("---------------selectMktsmsSendClientList() 结果为空，方法结束--------------------------");
                map = Tool.resultMap(CodeConts.DATA_IS_NUll, "结果为空");
                return map;
            }
        }

        logger.info("---------------selectMktsmsSendClientList() 获取成功，方法结束--------------------------");
        return map;
    }

    /**
     * 获取客户组成员
     * @param request
     * @param pageStart
     * @param pageSize
     * @param groupId
     * @return
     */
    @RequestMapping(value="selectMktsmsCustomerGroupMemberList",method= RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "获取客户组成员")
    public Map<String,Object> selectMktsmsCustomerGroupMemberList(HttpServletRequest request,
                                                                  @ApiParam(required = true, name = "pageStart", value = "起始页")@RequestParam(value = "pageStart")Integer pageStart,
                                                                  @ApiParam(required = true, name = "pageSize", value = "每页数量")@RequestParam(value = "pageSize")Integer pageSize,
                                                                  @ApiParam(required = true, name = "groupId", value = "客户组id")@RequestParam(value = "groupId")Long groupId){
        logger.info("---------------selectMktsmsCustomerGroupMemberList() 获取客户组成员--------------------------");
        logger.info("前端传入的参数为 [客户组id:" + groupId + "]");
        Map<String, Object> map = new HashMap<String, Object>();

        //查询当前用户信息
        Cookie cookie = Tool.ReadCookieMap(request).get("token");
        if (cookie == null || StringUtil.isBlank(cookie.getValue())) {
            return Tool.resultMap(CodeConts.DATA_IS_NUll, "用户未登录");
        }

        //判断营销短信id
        if (groupId == null) {
            logger.info("---------------selectMktsmsCustomerGroupMemberList() 非法参数，方法结束--------------------------");
            return Tool.resultMap(CodeConts.PARAM_LEGAL, "营销短信id不能为空");
        }
        //判断起始页和页面大小是否为空
        if (pageStart == null) {
            //设置默认起始页面，为1，即首页
            pageStart = 1;
        }
        if (pageSize == null) {
            //设置默认页面大小，为10
            pageSize = 10;
        }

        //判断起始页面和页面大小是否非法
        if (pageStart < 1 || pageSize < 0) {
            map.put("status", CodeConts.PARAM_LEGAL);
            map.put("message", "非法请求，请重试!");
            logger.info("查询参数非法，起始页码或页面大小错误");
        } else {
            List<Customer> pageInfo = null;
            try {

                pageInfo = mktsmsMessageService.selectMktsmsCustomerGroupMemberListByGroupId(pageStart, pageSize, groupId);

            } catch (Exception e) {
                logger.info("获取营销异常，异常信息为:" + e.getMessage());
                map = Tool.resultMap(CodeConts.FAILURE, "获取营销异常");
                logger.info("-----------------selectMktsmsCustomerGroupMemberList() 方法执行结束----------------");
                return map;
            }
            //判断结果
            if (pageInfo != null && !(pageInfo.size() > 0)) {

                map = Tool.resultMap(CodeConts.SUCCESS, "获取成功");
                map.put("page", pageInfo);
                logger.info("一共查询出" + pageInfo.size() + "条记录");
            } else {
                logger.info("---------------selectMktsmsCustomerGroupMemberList() 结果为空，方法结束--------------------------");
                map = Tool.resultMap(CodeConts.DATA_IS_NUll, "结果为空");
                return map;
            }
        }
        logger.info("---------------selectMktsmsCustomerGroupMemberList() 获取成功，方法结束--------------------------");
        return map;
    }


//    /**
//     * 营销短信编辑
//     * @param request
//     * @param mktsmsMessageVO
//     * @return
//     */
//    @RequestMapping(value="updateMktsmsMessage",method= RequestMethod.POST)
//    @ResponseBody
//    @ApiOperation(httpMethod = "POST", value = "营销短信编辑")
//    @ApiImplicitParams({@ApiImplicitParam(name = "excelFile", value = "上传文件", required = false, dataType = "file",paramType ="form")})
//    public Map<String,Object> updateMktsmsMessage(HttpServletRequest request,
//                                                  @ApiParam(required = true,name = "mktsmsMessageVO", value = "营销短信信息")@ModelAttribute("mktsmsMessageVO") MessageQueryPO mktsmsMessageVO
//    ){
//        logger.info("---------------updateMktsmsMessage() 营销短信编辑--------------------------");
//        logger.info("前端传入的参数为 [营销短信id:"+mktsmsMessageVO.getSmsId()+"]");
//        Map<String,Object> map=new HashMap<String,Object>();
//
//        try {
//
//
//
////            //参数校验
////            if(mktsmsMessageVO == null){
////                return Tool.resultMap(CodeConts.PARAM_LEGAL, "营销短信参数为空");
////            }
////            if(mktsmsMessageVO.getSmsId() == null){
////                return Tool.resultMap(CodeConts.PARAM_LEGAL, "营销短信Id为空");
////            }
////            if(StringUtil.isBlank(mktsmsMessageVO.getContent())){
////                return Tool.resultMap(CodeConts.PARAM_LEGAL, "营销短信短信内容不能为空");
////            }
////            if(mktsmsMessageVO.getSign() == null){
////                return Tool.resultMap(CodeConts.PARAM_LEGAL, "营销短信短信签名不能为空");
////            }
////            //判断是保存、提交
////            if("0".equals(mktsmsMessageVO.getStatus()) || "1".equals(mktsmsMessageVO.getStatus())){
////            }else{
////                logger.info("---------------updateMktsmsMessage() 非法参数，方法结束！--------------------------");
////                return Tool.resultMap(CodeConts.PARAM_LEGAL, "非法参数");
////            }
//
//            Message mktsmsMessage = new Message();
//            BeanUtils.copyProperties(mktsmsMessageVO,mktsmsMessage);
//
//            int update = 0;
//            //获取营销短信的发送方式
//            Message mktsmsMessageOriginal = mktsmsMessageService.selectMktSmsById(mktsmsMessage.getId());
//            //参数校验
//            if(mktsmsMessageOriginal == null){
//                throw new MessageSendException(CodeConts.DATA_IS_NUll,"营销短信不存在");
//            }
//            //判断营销短信的发送方式
//            if(mktsmsMessageOriginal.getSendWay() == 1){
//                //选择客户组
//                logger.info("---------------updateMktsmsMessage() 该营销短信发送方式是选择客户组方式--------------------------");
//                if(mktsmsMessage.getCustGroup() == null){
//                    return Tool.resultMap(CodeConts.DATA_IS_NUll, "营销短信客户组不能为空");
//                }
//
//                map = mktsmsMessageService.updateMktSmsChooseById(mktsmsMessage,mktsmsMessageVO.isDistinct(),mktsmsMessageVO.isWrong());
//                update = (Integer)map.get("update");
//
//            }else if(mktsmsMessageOriginal.getSendWay() == 2){
//                //导入客户
//                logger.info("---------------updateMktsmsMessage() 该营销短信发送方式是录入客户方式--------------------------");
//                /*if(StringUtil.isBlank(mktsmsMessageOriginal.getDataFile()) && mktsmsMessageVO.getExcelFile() == null && StringUtil.isBlank(mktsmsMessage.getDataFile())){
//                        return Tool.resultMap(CodeConts.DATA_IS_NUll, "营销短信导入文件不能为空");
//                }*/
//                if(mktsmsMessageVO.getExcelFile()== null && StringUtil.isBlank(mktsmsMessage.getDataFile())){
//                    logger.info("---------------updateMktsmsMessage() 非法参数，方法结束！--------------------------");
//                    return Tool.resultMap(CodeConts.PARAM_LEGAL, "非法参数");
//                }
//
//                if(mktsmsMessageVO.getExcelFile() != null){
//                    //判断文件大小是否小于50M
//                    long fileSize = mktsmsMessageVO.getExcelFile().getSize();
//                    if (fileSize > 50 * 1024 * 1024) {
//                        logger.info("文件大小超出50M!");
//                        map = Tool.resultMap(CodeConts.FAILURE, "录入文件大小不能超过50M!");
//                        logger.info("----------营销短信编辑结束----------");
//                        return map;
//                    }
//                    //获取文件全名
//                    String excelFileName=mktsmsMessageVO.getExcelFile().getOriginalFilename();
//                    //获取文件扩展名
//                    String fileExtension = excelFileName.substring(excelFileName.lastIndexOf(".") + 1);
//                    //判断文件类型（扩展名）是否满足要求
//                    if(fileExtension.equals("xls") || fileExtension.equals("xlsx")){
//                    }else {
//                        logger.info("---------------updateMktsmsMessage() 文件格式不正确，方法结束！--------------------------");
//                        return Tool.resultMap(CodeConts.PARAM_LEGAL, "文件格式不正确");
//                    }
//                    /*CommonsMultipartFile cf= (CommonsMultipartFile)mktsmsMessageVO.getExcelFile();
//                    DiskFileItem fi = (DiskFileItem)cf.getFileItem();
//                    mktsmsMessage.setDataFile(mktsmsMessageVO.getExcelFile().getOriginalFilename());
//                    File f = fi.getStoreLocation();*/
//                    InputStream inputStream = mktsmsMessageVO.getExcelFile().getInputStream();
//                    List<SendRecord> list = FileParse.smsFileParse(inputStream,mktsmsMessageVO.getExcelFile().getOriginalFilename());
//                    if(list.size() == 0){
//                        throw new MessageSendException(CodeConts.FAILURE,"客户成员不能为空");
//                    }
//                    map = mktsmsMessageService.updateMktSmsEnteringById(mktsmsMessage,list);
//                    update = (Integer)map.get("update");
//                }else{
//                    map = mktsmsMessageService.updateMktSmsEnteringById(mktsmsMessage,null);
//                    update = (Integer)map.get("update");
//                }
//
//            }else{
//                return Tool.resultMap(CodeConts.PARAM_LEGAL, "营销短信发送方式不存在!");
//            }
//
//            //判断结果
//            if(update>0){
//                map.put("status",CodeConts.SUCCESS);
//                map.put("message","编辑营销短信成功!");
//                logger.info("---------------updateMktsmsMessage() 营销短信编辑成功，方法结束！--------------------------");
//            }else {
//                map.put("status",CodeConts.DATA_IS_NUll);
//                map.put("message","编辑营销短信失败!");
//                logger.info("---------------updateMktsmsMessage() 营销短信编辑失败，方法结束！--------------------------");
//            }
//
//        }catch (MessageSendException e) {
//            return Tool.resultMap(e.getErrorCode(),e.getMessage());
//        }catch (MessageSendException e){
//            logger.error("delectMktsmsBySmsId()  error:", e);
//            return Tool.resultMap(e.getErrorCode(), e.getLocalizedMessage());
//        }catch (Exception e){
//            logger.error("--------营销短信编辑异常："+e.getMessage()+"----------");
//            map=Tool.resultMap(CodeConts.INSERT_DB_AFTER_ERR,"营销短信编辑异常");
//            e.printStackTrace();
//        }
//
//        return map;
//
//    }

    @RequestMapping(value="selectCountByStatus",method= RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "获取短信数量")
    public Map<String,Object> selectCountByStatus(HttpServletRequest request){
        logger.info("---------------selectCountByStatus() 获取短信数量--------------------------");
        Map<String,Object> map=new HashMap<String,Object>();
        


        int count = mktsmsMessageService.selectCountByStatus("0","");

        map.put("status",CodeConts.SUCCESS);
        map.put("message","获取成功");
        map.put("mktsmsNum",count);
        logger.info("---------------selectCountByStatus() 获取短信数量，方法结束！--------------------------");
        return map;
    }

}
