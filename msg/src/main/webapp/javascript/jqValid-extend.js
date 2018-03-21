function setDefaultValidate(){
    $.extend(true, $.validator, {
            // 方法
            methods: {
                //手机号
                "verifyPhone": function (value, element) {
                    var mobile = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0-9]|17[0-9])\d{8}$/;
                    return this.optional(element) || (mobile.test(value));
                },
                //固话,法人联系电话
                "verifyTel": function (value, element) {
                    var mobile = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0-9]|17[0-9])\d{8}$/;
                    var tel =  /^0\d{2,3}-?\d{7,8}$/;
                    return this.optional(element) || (tel.test(value))||(mobile.test(value));
                },
                //银行卡号
                "verifyCashCard": function (value, element) {
                    var reg =/^([\d]){16,30}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //邮箱
                "verifyMail": function (value, element) {
                    var mail = /^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$/;
                    return this.optional(element) || (mail.test(value));
                },
                //登录密码
                "verifyLoginPassword": function (value, element) {
                    var regPwd= /^(?=.*\d.*\d)+(?=.*[a-z|A-Z]*.[a-z|A-Z]).{6,12}$/;
                    return this.optional(element) || (regPwd.test(value));
                },
                //交易密码，确认密码
                "verifyCashPassword": function (value, element) {
                    var regPwd= /^([\d]){6}$/;
                    return this.optional(element) || (regPwd.test(value));
                },
                //证件号码
                "verifyIDCode": function (value, element) {
                    var reg= /^([a-z|A-Z|()|+|0-9]){1,28}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //验证码
                "verifyVerifyCode": function (value, element) {
                    var reg= /^([\d]){6}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //理顾工号
                "verifyAdvisorID": function (value, element) {
                    var reg= /^H\d{6}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //申请编号
                "verifyApplyID": function (value, element) {
                    var reg= /^([\d]){1,50}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //资质编号，产品备案编号
                "verifyZizhiID": function (value, element) {
                    var reg= /^[\w|a-z|A-Z|\d]{1,30}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //注册资本
                "verifyRegZiben": function (value, element) {
                    var reg= /^\d{1,14}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //法人职务
                "verifyLegalTitle": function (value, element) {
                    var reg= /^[\u4e00-\u9fa5a-zA-Z]{1,30}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //邮编
                "verifyMailCode": function (value, element) {
                    var reg= /^\d{6}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //产品规模
                "verifyProductScale": function (value, element) {
                    var reg= /^\d{1,14}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //产品存续期
                "verifyProductExpire": function (value, element) {
                    var reg= /^\d{1,4}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //客户编号
                "verifyClientId": function (value, element) {
                    var reg=/^\d{1,50}$/;
                    return this.optional(element) || (reg.test(value));
                },
                //说明
                "verifyDescription": function (value, element) {
                    var reg=/^[A-Za-z\u4e00-\u9fa5]{1,300}$/;
                    return this.optional(element) || (reg.test(value));
                }
            },
            // 根据规则添加
            classRuleSettings: {
                required:{
                    required:true
                },
                verifyPhone: {
                    verifyPhone: true
                },
                verifyTel: {
                    verifyTel: true
                },
                verifyMail: {
                    verifyMail: true
                },
                verifyLoginPassword: {
                    verifyLoginPassword: true
                },
                verifyCashPassword: {
                    verifyCashPassword: true
                },
                verifyIDCode: {
                    verifyIDCode: true
                },
                verifyVerifyCode: {
                    verifyVerifyCode: true
                },
                verifyAdvisorID: {
                    verifyAdvisorID: true
                },
                verifyApplyID: {
                    verifyApplyID: true
                },
                verifyZizhiID: {
                    verifyZizhiID: true
                },
                verifyRegZiben: {
                    verifyRegZiben: true
                },
                verifyLegalTitle: {
                    verifyLegalTitle: true
                },
                verifyMailCode: {
                    verifyMailCode: true
                },
                verifyProductScale: {
                    verifyProductScale: true
                },
                verifyProductExpire: {
                    verifyProductExpire: true
                },
                verifyClientId: {
                    verifyClientId: true
                },
                verifyDescription: {
                    verifyDescription: true
                }
            },
            // 方法默认提示信息
            messages: {
                required: '不能为空！',
                verifyMobile: "请填写正确的手机号！",
                verifyMail: "请填写正确的邮箱！",
                verifyPassword: "6-12位，至少包含2个数字，2个字母！",
                verifyCode: "请填写正确的证件号码！",
            },
            // 默认选项
            defaults: {
                onkeyup: false,
                // 验证规则
                rules: {
                },
                // 验证失败的信息
                messages: {
                },
                // 错误被包裹的标签
                errorElement: 'span',
                // 忽略的元素
                ignore: '',
                // 错误标签显示的位置
                errorPlacement: function (error, element) {
                    var ulDom = element.closest("ul");
                    // 标签隐藏就展开
                    if (ulDom.is(":hidden")) {
                        ulDom.prev().find("label:contains('展开')").click();
                    }
                    // 如果是验证码(下拉框后面跟按钮或者其他)，select要增加class为 verifySelect
                    if (element.hasClass('verifySelect')) {
                        element.parents('.dropDiv').next().after(error);
                    }
                    // 如果是下拉框
                    else if (element[0].tagName == 'SELECT') {
                        element.parent().after(error);
                    } else {
                        error.insertAfter(element);
                    }
                },
                // 提交事件
                submitHandler: function () {
                }
            }

        });
}
