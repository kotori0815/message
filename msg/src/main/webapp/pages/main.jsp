<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>持名法州主页</title>
    <link rel="stylesheet" type="text/css" href="../themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="../themes/IconExtension.css">
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../js/datagrid-detailview.js"></script>
    <script type="text/javascript" src="../js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/echarts.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/china.js"></script>


    <script type="text/javascript" src="${pageContext.request.contextPath}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/ueditor/ueditor.all.js"></script>

</head>
<body class="easyui-layout">
<div data-options="region:'north',split:true" style="height:60px;background-color:  #5C160C">

    <div style="font-size: 24px;color: #FAF7F7;font-family: 楷体;font-weight: 900;width: 500px;float:left;padding-left: 20px;padding-top: 10px">
        持名法州后台管理系统
    </div>
    <div style="font-size: 16px;color: #FAF7F7;font-family: 楷体;width: 300px;float:right;padding-top:15px">
        欢迎您:${sessionScope.admin.adminName} &nbsp;<a href="#" class="easyui-linkbutton"
        data-options="iconCls:'icon-edit'">修改密码</a>&nbsp;&nbsp;<a href="#"
                                                                                                               class="easyui-linkbutton"
        data-options="iconCls:'icon-01'">退出系统</a>
    </div>
</div>
<div data-options="region:'south',split:true" style="height: 40px;background: #5C160C">

    <div style="text-align: center;font-size:15px; color: #FAF7F7;font-family: 楷体">&copy;百知教育 gaozhy@zparkhr.com.cn
    </div>
</div>

<div data-options="region:'west',title:'导航菜单',split:true" style="width:220px;">
    <div id="aa" class="easyui-accordion" data-options="fit:true">

    </div>
</div>
<div data-options="region:'center'">
    <div id="tt" class="easyui-tabs" data-options="fit:true,narrow:true,pill:true">
        <div title="主页" data-options="iconCls:'icon-neighbourhood',"
        style="background-image:url(image/shouye.jpg);background-repeat: no-repeat;background-size:100% 100%;"></div>

    </div>
</div>
<script type="text/javascript">
    $(function () {
        $.get(
            "${pageContext.request.contextPath}/menu/queryMenus",
            function (resu) {
                for (var i = 0; i < resu.length; i++) {
                    var sonMenus = resu[i].menus;
                    var line = "";
                    for (var j = 0; j < sonMenus.length; j++) {
                        line = line + "<p style=\"text-align: center\"><a class='easyui-linkbutton' onclick=\"addMainTabs('"
                            + sonMenus[j].title + "','" + sonMenus[j].iconname + "','"
                            + sonMenus[j].path + "')\" data-options=\"iconCls:'icon-"
                            + sonMenus[j].iconname + "'\">"
                            + sonMenus[j].title + "</a></p>";
                    }
                    $("#aa").accordion("add", {
                        title: resu[i].title,
                        content: line,
                        selected: false,
                        iconCls: "icon-" + resu[i].iconname
                    })
                }
            }, "json"
        )
    })


    function addMainTabs(title, iconname, menuPath) {
        console.log(title);
        console.log(iconname);
        console.log(menuPath);
        if(!$("#tt").tabs("exists",title)){
            //创建tab
            $("#tt").tabs("add",{
                title:title,
                iconCls:"icon-"+iconname,
                closable:true,
                href:"${pageContext.request.contextPath}/"+menuPath
            });

        }else{
            //已选中
            $("#tt").tabs("select",title);
        }
    }
</script>
</body>
</html>
