<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.net.URLDecoder" %><%--
  Created by IntelliJ IDEA.
  User: wd199
  Date: 2017/6/12
  Time: 14:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>持名法州后台管理中心</title>

    <link rel="stylesheet" type="text/css" href="../themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="../themes/IconExtension.css">
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../js/datagrid-detailview.js"></script>
    <script type="text/javascript" src="../js/easyui-lang-zh_CN.js"></script>
    <link rel="icon" href="../img/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="../css/common.css" type="text/css"/>
    <link rel="stylesheet" href="../css/login.css" type="text/css"/>
    <script type="text/javascript" src="../script/jquery.js"></script>
    <script type="text/javascript" src="../script/common.js"></script>
    <script type="text/javascript">
<%
String username=null;
Cookie[] cookies=request.getCookies();
if(cookies!=null){
    for (Cookie ck:cookies ){
        if (ck.getName().equals("username")){
            username=ck.getValue();
            username=URLDecoder.decode(username,"utf-8");
        }
    }
}

if (username!=null)
    pageContext.setAttribute("username",username);
    else
    pageContext.setAttribute("username","");
%>
        $(function(){
            //点击更换验证码：
            $("#captchaImage").click(function(){//点击更换验证码
                $("#captchaImage").get(0).src="${pageContext.request.contextPath}/admin/validate?time="+new Date().getTime()
            });

            //  form 表单提交
            $("#loginForm").on("submit",function(){
                $.ajax({
                    type:"post",
                    url:"${pageContext.request.contextPath}/admin/login",
                    data:$("#loginForm").serialize(),
                    success:function (msg) {
                        alert(msg);
                      if (msg==="true"){
                          location.href="../main/main.jsp"
                      }
                    }
                })
                return false;
        });
        });
    </script>
</head>
<body>

<div class="login">
    <form id="loginForm">

        <table>
            <tbody>
            <tr>
                <td width="190" rowspan="2" align="center" valign="bottom">

                </td>
                <th>
                    用户名:
                </th>
                <td>
                    <input type="text" class="easyui-validatebox" data-options="required:true" name="adminName" class="text" value="${username}"  maxlength="20"/>
                </td>
            </tr>
            <tr>
                <th>
                    密&nbsp;&nbsp;&nbsp;码:
                </th>
                <td>
                    <input type="password" class="easyui-validatebox" data-options="required:true" name="password" class="text" maxlength="20" autocomplete="off"/>
                </td>
            </tr>

            <tr>
                <td>&nbsp;</td>
                <th>验证码:</th>
                <td>
                    <input type="text" class="easyui-validatebox" data-options="required:true" id="enCode" name="vcode" class="text captcha" maxlength="4" autocomplete="off"/>
                    <img id="captchaImage" class="captchaImage" src="${pageContext.request.contextPath}/admin/validate" title="点击更换验证码"/>
                </td>
            </tr>
            <tr>
                <td>
                    &nbsp;
                </td>
                <th>
                    &nbsp;
                </th>
                <td>
                    <label>
                        <input type="checkbox" id="isRememberUsername" name="sign" value="true"/> 记住用户名
                    </label>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <th>&nbsp;</th>
                <td>
                    <input type="button" class="homeButton" value="" onclick="location.href='/'"><input id="sub" type="submit" class="loginButton" value="登录">
                </td>
            </tr>
            </tbody></table>
            <div>
                <%

                %>
            </div>
    </form>
</div>
</body>
</html>