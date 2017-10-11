<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>首页布局</title>
</head>
<%@include file="/commons/statics.jsp"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/default.css">
<script type="text/javascript" src='./js/easyui/outlook2.js'> </script>
<script>
	 
				var result = $.ajax({
					  url: "menuinfo/initMenu.do",
					  async: false
					 }).responseText;
				
				var _menus = eval('('+result+')');
				
				
        	//设置登录窗口
            function openPwd() {
               $('#w').window({
                   title: '修改密码',
                   width: 300,
                   modal: true,
                   shadow: true,
                   closed: true,
                   height: 160,
                   resizable:false
               });
           } 
         function closeLogin() {
             $('#w').window('close');
         }
        $(function() {
        	openPwd();
           //关闭登录窗口
           
           $('#editpass').click(function() {
               $('#w').window('open');
           })

           $('#btnEp').click(function() {
               serverLogin();
           })
           var contextPath='${pageContext.request.contextPath}';
          
           //修改密码
           function serverLogin() {
               var $newpass = $('#txtNewPass');
               var $rePass = $('#txtRePass');

               if ($newpass.val() == '') {
                   msgShow('系统提示', '请输入密码！', 'warning');
                   return false;
               }
               if ($rePass.val() == '') {
                   msgShow('系统提示', '请在一次输入密码！', 'warning');
                   return false;
               }

               if ($newpass.val() != $rePass.val()) {
                   msgShow('系统提示', '两次密码不一至！请重新输入', 'warning');
                   return false;
               }
               $.post(contextPath+'/userinfo/newpass.do?newpass=' + $newpass.val(), function(msg) {
                   msgShow('系统提示', '恭喜，密码修改成功！<br>您的新密码为：' + msg, 'info');
                   $newpass.val('');
                   $rePass.val('');
                   close();
               })
               
           }

           $('#loginOut').click(function() {
               $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {

                   if (r) {
                   	var logOut = contextPath+'/loginOut.do';
                       $.post(logOut,function(result){
               			if(result.success){
               				window.location.href = contextPath+"/login.do";
               			}
               		});
                   }
               });
           })	
           
           $('body').keydown(function(e) {
      	     if (e.keyCode == 13) {
      	         e.preventDefault();
      	         e.stopPropagation();
      	     }
      	 })
        });
        function changeTheme(theme){
        	changeThemeFun(theme);
        }
    </script>
    <body class="easyui-layout" style="overflow-y: hidden"  scroll="no">
<noscript>
<div style=" position:absolute; z-index:100000; height:2046px;top:0px;left:0px; width:100%; background:white; text-align:center;">
    <img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
</div></noscript>
    <div region="north"  border="false" style="BACKGROUND: #E6E6FA; height: 50px; padding: 1px; overflow: hidden;">
        <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        	<tr>
        		<td align="left" style="vertical-align: text-bottom;" valign="middle">
        			<span style="padding-left:20px;padding-top:10px; font-size: 30px; font-family:STSong">&nbsp;&nbsp;资产负债管理</span>
        		</td>
        		<td align="right" nowrap>
        			<table>
        				<tr>
			        		<td valign="top" height="50">
				        			<span style="color: #CC33FF;font-family: Verdana, 微软雅黑,黑体">欢迎 ${currentUser.userName} ${role.roleName}</span>
				        			<a href="#" id="bgclock">日期</a>  
			        		</td>
        				</tr>
		        		<tr>
			        		<div style="position: absolute; right: 10px; bottom: 0px;">
	               						<a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'" href="#" id="editpass">修改密码</a> 
	               						<!-- <a class="easyui-menubutton" data-options="menu:'#mm1',iconCls:'icon-help'"  href="#" id="changeTheme">切换主题</a>  -->
								        <a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-back'"  href="#" id="loginOut">安全退出</a>
               				</div>
               				<!-- <div id="mm1" style="width:150px;">
								<div onclick="changeTheme('default')">默认主题</div>
								<div class="menu-sep"></div>
								<div onclick="changeTheme('bootstrap')">bootstrap风格</div>
								<div class="menu-sep"></div>
								<div onclick="changeTheme('gray')">灰色风格</div>
								<div class="menu-sep"></div>
								<div onclick="changeTheme('metro')">metro风格</div>
								<div class="menu-sep"></div>
								<div onclick="changeTheme('pepper-grinder')">pepper-grinder</div>
								<div class="menu-sep"></div>
								<div onclick="changeTheme('cupertino')">cupertino</div>
								<div class="menu-sep"></div>
								<div onclick="changeTheme('dark-hive')">dark-hive</div>
								<div class="menu-sep"></div>
								<div onclick="changeTheme('sunny')">sunny</div>
							</div> -->
               			</tr>
        			</table>
        	</tr>
        </table>
    </div>
    <div region="south" split="true" style="height: 30px; background: #D2E0F2; ">
        <div class="footer">中科软科技股份有限公司</div>
    </div>
    <div region="west" split="true" title="导航菜单" style="width:180px;" id="west">
<div class="easyui-accordion"  fit="true" border="false">
		<!--  导航内容 -->
 </div>

    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
			<div title="首页" style="padding:20px;overflow:hidden;" id="home">
				<shiro:hasRole name="admin">
				    用户拥有角色
				</shiro:hasRole>
			<h1 style="font-size:15px">欢迎登陆资产负债管理系统!</h1>

			</div>
		</div>
    </div>
    
    
    <!--修改密码窗口-->
    <div id="w" class="easyui-window" title="修改密码" collapsible="false" minimizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <table cellpadding=3>
                    <tr>
                        <td>新密码：</td>
                        <td><input id="txtNewPass" type="newPassword" class="txt01" /></td>
                    </tr>
                    <tr>
                        <td>确认密码：</td>
                        <td><input id="txtRePass" type="confirmPassword" class="txt01" /></td>
                    </tr>
                </table>
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" >
                    确定</a> <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"
                        onclick="closeLogin()">取消</a>
            </div>
        </div>
    </div>

	<div id="mm" class="easyui-menu" style="width:150px;">
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-exit">退出</div>
	</div>
</body>
</html>