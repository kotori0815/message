/**
 * jQuery 扩展函数
 * 
 * jquery-extend - v1.0.0 - 2017-08-9 Copyright (c) 2017 vicky
 */

(function($){
	// 向head标签里加入新增的分页插件样式文件
	if(!$('#layer-extend').attr('id')){
		$(document.head).append('<link id="layer-extend" rel="stylesheet" href="../css/core/layer-extend.css">');
	}
	$.layer={

			/********************************alert********************************/
            // 所有提示信息(包含成功失败信息)
            alertE : function(text, title,callBack) {
                return $.layer.alert("<div>"+text+"</div>", {
                    title : title,
                    closeBtn: 0, //去掉右边的×
                    resize : false,
                    shift: -1,//关闭弹出框花式弹出效果
                    zIndex : layer.zIndex,
                    anim : Math.ceil(Math.random() * 6),
                    end: function() {
                        if(typeof callBack == 'function') {
                            callBack();
                        }
                    },
					success:function(layer){
                        layer.find('.layui-layer-content').css({'display':'table','width':'90%','text-align':'center'});
                        layer.find('.layui-layer-content>div').css({'display':'table-cell','width':'100%','vertical-align':'middle','font-size':'16px'});
                        layer.find('.layui-layer-content>div>div').css({'font-size':'16px'});
                    }
                });
            },
			alert:function(text,options){
				if(!options){
					options= {
							area: ['400px','260px'],
							time : 5000,
							resize : false,
							shift: -1,//关闭弹出框花式弹出效果
							zIndex : layer.zIndex,
							anim : Math.ceil(Math.random() * 6)
						}
				}
				options.area = ['400px','230px'];
						
				return parent.layer.alert("<div>"+text+"</div>",options);
			},

			// 信息提示,不带图标
			alertI : function(text) {
				return $.layer.alert("<div>"+text+"</div>");
			},
			/********************************msg********************************/
			//信息提示不带按钮
			msg:function(context,options){
				return parent.layer.msg(context,options);
			},
			//信息提示 错误图标
			msgE:function(context,options){
				return $.layer.msg(context,{
					icon:2,
					shade :0.1,
					shadeClose:true
				});
			},
			/********************************load********************************/
			//加载层
			load:function(icon,options){
				if(!options){
					options={};
					options.shade=0.1;
				}
				return parent.layer.load(icon,options);
			},
			close:function(index)
			{
				parent.layer.close(index);
			},
			/********************************confirm********************************/
			confirmContext:function(content,title,btn,callBack,options){
				console.log(callBack);
				if(!options){
					options={};
				}
				options.title=title;
				options.resize=false;
				options.btn=btn;
				options.btnAlign='c';
				parent.layer.confirm(content, options, function(index) {
					callBack(index);
				});
			},
			/********************************open********************************/
			open:function(options){
				return parent.layer.open(options);
			},
			//打开一个url
			openUrl:function(url,title,options){
				if(!title) title="信息";
				if(!options){
					options={};
					options.area=["850px","600px"];
				}
				options.type=2;
				options.content=url;
				options.title=title;
				options.btnAlign='c';
                options.resize=false;
				return $.layer.open(options);
			},
			//打开一个div
			openContent:function(content,title,options){
				if(!title) title="信息";
				if(!options){
					options={};
					options.area=["550px","400px"];
				}
				options.type=1;
				options.content=content;
				options.title=title;
				options.btnAlign='c';
				options.resize=false;
				return $.layer.open(options);
			},
			
			//打开一个tab
			openTab:function(url,options){
				if(!options){
					options={};
					options.area=["850px","600px"];
				}
				options.type=2;
				options.content=url;
				options.btnAlign='c';
				return $.layer.open(options);
			}
	}
	
}(jQuery));