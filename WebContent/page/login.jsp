<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<%@include file="/commons/taglibs.jsp"%>
<html lang="zh-CN">
<style type="text/css">
*   .* {
	margin: 0;
	padding: 0;
}
body, button, input, select, textarea, h1, h2, h3, h4, h5, h6 ,span,a{
	font-family: Microsoft YaHei, '宋体', Tahoma, Helvetica, Arial,
		"\5b8b\4f53", sans-serif;
	
	font-size:14px;
}
body {
	margin: 0;
	padding: 0;
}

.head {
	height: 30px;
	width: 100%;
	background: #f2f2f2;
	padding: 0;
	margin: 0;
}

.logo {
	height: 90px;
	width: 960px;
	margin: 0 auto;
	overflow: hidden;
	clear: both;
}

.logo img {
	height: 90px;
	width: 200px;
	overflow: hidden;
	float: left;
}

.logo div {
	font-size: 28px;
	color: #666;
	height: 40px;
	float: left;
	line-height: 60px;
	margin: 20px 10px;
	padding: 10px;
	/* border-left: 1px solid #d2d2d2; */
}

.login_con {
	width: 960px;
	height: 332px;
	margin: 10px auto;
	margin-bottom:50px;
	clear: both;
	
}

.login_con_L {
	float: left;
	width: 568px;
	height: 332px;
	overflow: hidden;
}

.login_con_R {
	float: left;
	width: 376px;
	height: 332px;
	border: 1px solid #dce7f4;
}

.login_con_R h4 {
	background: #F2F2F2;
	line-height: 36px;
/* 	width: 376px;
 */	padding: 0px 6px;
	border: 1px solid #fff;
	border-bottom: 1px solid #d4d4d4;
	margin-top: 0px;
}

.login_con_R  form {
	position: relative;
	padding-top: 10%;
	padding-left: 7%;
	padding-right: 7%;
}
.login_con_R .input-group {
    width: 80%;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 25px!important;
}
.checkCode {
	position: absolute;
	top: 54%;
	left: 56%;
	height: 50px;
}
.btn-login {
	width: 100%;
	margin-left: auto;
    margin-right: auto;
    font-size: 17px;
    font-weight: bold;
    letter-spacing: 5px;
}

.login_footer {
	clear: both;
	margin: 8% auto 0;
	width: 300px;
	color: inherit;
    font-size: 21px;
    font-weight: 200;
    line-height: 2.14286;
}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆</title>
<%@include file="/commons/statics.jsp"%>
<%-- <link href="<c:url value='/css/login.css' />" rel="stylesheet" type="text/css"> --%>
<script   language="javascript">     
      if (top != window)     
      top.location.href = window.location.href;     
  </script>
</head>
<body style="padding:0;">
	<div class="head login" style="background: #C3AB83;"></div>
	<div class="logo" style="margin-bottom: -20px;margin-top: 20px">
		<!-- <img src="images/logo1.jpg" style="height: 70px;"/> -->
		<div style="margin-top: 0px;Font-family: stxingkai; Font-size: 32pt; Font-style: bold; color: #000000;">资产负债管理系统</div>
	</div>
	<div class="login_con">
		<div class="login_con_L">
			<img src="images/backgr.jpg" height="342" width="550px"/>
		</div>
		<div class="login_con_R">
			<h4>登录</h4>
			<form id="fm" method="post">
				<div class="form-group input-group">
                    <span class="input-group-addon" style="color:#0D9E67;"><span class="glyphicon glyphicon-user"></span></span>
                	<input type="text" class="easyui-textbox"  data-options="iconCls:'icon-man',prompt:'请输入用户名',events:{keydown:function(e){if(e.keyCode==13)loginIn();}}" style="width:205px; height:36px; border:1px solid #cad2db;padding:0 5px;"  name="username" class="easyui-validatebox"  required="true" >
				</div>
                <div class="form-group input-group">
                    <span class="input-group-addon" style="color:#0D9E67;"><span class="glyphicon glyphicon-lock"></span></span>
					<input type="password" class="easyui-textbox easyui-validatebox" id="password" data-options="iconCls:'icon-lock',prompt:'请输入密码',events:{keydown:function(e){if(e.keyCode==13)loginIn();}}" style="width:205px; height:36px; border:1px solid #cad2db;padding:0 5px;" name="password" required="true">
                </div>
				<div class="form-group input-group" style="margin-top: 45px;">
			    	<a href="#" class="easyui-linkbutton" style=" width:205px; height:32px;background:#F2F2F2; margin:10px auto;"  onclick="loginIn()">登录</a>
				</div>
			</form>
		</div>
	</div>
	<div>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" style="font-size: 14px;margin-top: 100px;">
			<TBODY>
				<TR>
					<TD height=35 align=center>
						&nbsp;&nbsp;<SPAN class=bai_zi><!-- img src="./images/background/head_left_gs.gif"-->中科软科技股份有限公司版权所有</SPAN><SPAN class=bai_zi>&nbsp;&nbsp;</SPAN>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</div>
<script type="text/javascript">
    	var contextPath = '${pageContext.request.contextPath}';
    	//官方给的textbox事件绑定但是无效。所以用的dataoption属性；
    	/* $("#password").textbox('textbox').bind('keydown', function(e){
    		alert();
    		if (e.keyCode == 13){	// when press ENTER key, accept the inputed value.
    			t.textbox('setValue', $(this).val());
    		}
    	}); */
    	//全局的回车事件与密码的回车事件可二选一，也可并存。
    	//如果需要全局的放开即可
    	/* $(function(){
    		document.onkeydown = function(e){ 
    		    var ev = document.all ? window.event : e;
    		    if(ev.keyCode==13) {
    		    	loginIn();
    		     }
    		}
    		}); */  
		function loginIn() {
			$('#fm').form('submit',{
        		url: '${pageContext.request.contextPath}/login.do',
        		onSubmit: function(){
        			return $(this).form('validate');
        		},
        		success: function(data){
        			eval("result="+data);
        			//var result=$.parseJSON(result);
        			if(result.success){
        				 $.messager.show({
        					type: 'success',
        					content:  '登录成功！'
        				}); 
        				window.location.href=contextPath+"/index.do";
        			}else{
        				$.messager.show({
        					title: '登录失败',
        					msg: result.errorMsg
        				});
        			}
        		}
        	});
		}
	</script>
</body>
</html>