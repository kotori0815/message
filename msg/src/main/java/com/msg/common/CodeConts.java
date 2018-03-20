package com.msg.common;

/**
 * Date: 2017/7/20
 * Time: 15:42
 * User: yangkai
 * EMail: yangkai01@chtwm.com
 */
public class CodeConts {

    /** 操作成功 */
    public final static String SUCCESS = "0000";
    /** 操作成功,数据为空 */
    public final static String DATA_IS_NUll = "1000";
    /** 操作失败 */
    public final static String FAILURE = "4000";
    /** 参数不合法:非法请求过来参数 */
    public final static String PARAM_LEGAL = "4001";
    /** 入库失败 */
    public final static String INSERT_DB_ERR = "4002";
    /** 无更新数据 */
    public final static String NOT_UPDATE_DATA = "4003";
    /** 存在敏感词汇 */
    public final static String EXISTS_SENSITIVE_WORD = "4004";

    /** 其他异常 */
    public final static String INSERT_DB_AFTER_ERR = "4006";
    /** 登录失败 */
    public final static String LOGIN_FAILURE = "4007";
    /** 返回值错误 */
    public final static String RESULT_FAIURE = "4008";
    /** 系统异常 */
    public final static String SYS_ERR = "4009";
    /**用户不存在*/
    public final static String CUSTOMER_NON = "4010";

    /**
     * 营销短信不存在
     */
    public final static String MKTSMS_IS_NULL="M1001";
    /**
     * 提交审核，已经提交
     */
    public final static String APPROVE_TO_APPROVE="M3101";
    /**
     * 提交审核，审核通过
     */
    public final static String APPROVE_IS_SUCCESS="M3102";
    /**
     * 提交审核，审核驳回
     */
    public final static String APPROVE_IS_FAILURE="M3103";
    /**
     * 删除短信待审核
     */
    public final static String Del_TO_APPROVE="M3104";
    /**
     * 删除短信审核通过
     */
    public final static String Del_IS_APPROVE="M3105";

    /**
     * 列表为空
     */
    public final static String LIST_IS_NULL="M4001";

    /**
     *
     */
    public final static String NUMBER_NOT_EQUAL="M4002";

    /**
     * 短信发送记录列表插入发送信息数量出现问题
     */
    public final static String INSERT_RECORD_LIST_NUMBER_WRONG="M5001";

    /**
     * 已经存在相同名字的客户组
     */
    public final static String CUSTOMER_GROUP_ALREADY_EXIST="CG0001";

    /**
     * 客户组名称无效
     */
    public final static String INVALID_CUSTOMER_GROUP_NAME="CG0002";

    /**
     * 客户组类别无效
     */
    public final static String INVALID_CUSTOMER_GROUP_TYPE="CG0003";

    /**
     * 创建人无效
     */
    public final static String INVALID_CUSTOMER_GROUP_CREATEID="CG0004";

    /**
     * 总页数
     */
    public final static String REDIS_TOTAL_PAGE="TOTAL_PAGE";
    /**
     * 当前页
     */
    public final static String REDIS_CURRENT_PAGE="CURRENT_PAGE";
    /**
     * 每页条数
     */
    public final static int QUERY_PAGE_SIZE=2000;

    /**
     * 短信发送至运营商时拆分的号码个数
     */
    public final static int MKTSMS_MESSAGE_NUM=10000;



}