<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页布局</title>
<script type="text/javascript" src="./js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="./js/easyui/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="./js/easyui/demo.css">
<link rel="stylesheet" type="text/css" href="./js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="./js/easyui/themes/icon.css">
</head>
<body>
    <div class="easyui-layout" style="width:400px;height:200px;">
    	<div region="west" split="true" title="Navigator" style="width:150px;">
    		<p style="padding:5px;margin:0;">Select language:</p>
    		<ul>
    			<li><a href="javascript:void(0)" onclick="showcontent('java')">Java</a></li>
    			<li><a href="javascript:void(0)" onclick="showcontent('cshape')">C#</a></li>
    			<li><a href="javascript:void(0)" onclick="showcontent('vb')">VB</a></li>
    			<li><a href="javascript:void(0)" onclick="showcontent('erlang')">Erlang</a></li>
    		</ul>
    	</div>
    	<div id="content" region="center" title="Language" style="padding:5px;">
    	</div>
    </div>
    <div class="easyui-tabs" style="width:400px;height:100px;">
    	<div title="First Tab" style="padding:10px;">
    		First Tab
    	</div>
    	<div title="Second Tab" closable="true" style="padding:10px;">
    		Second Tab
    	</div>
    	<div title="Third Tab" iconCls="icon-reload" closable="true" style="padding:10px;">
    		Third Tab
    	</div>
    </div>
        <div style="margin-bottom:10px">
    	<a href="#" class="easyui-linkbutton" onclick="addTab('google','http://www.google.com')">google</a>
    	<a href="#" class="easyui-linkbutton" onclick="addTab('jquery','http://jquery.com/')">jquery</a>
    	<a href="#" class="easyui-linkbutton" onclick="addTab('easyui','http://jeasyui.com/')">easyui</a>
    </div>
    <div id="tt" class="easyui-tabs" style="width:400px;height:250px;">
    	<div title="Home">
    	</div>
    </div>
    
    <script type="text/javascript">
    function showcontent(language){
    	$('#content').html('Introduction to ' + language + ' language');
    }
    function addTab(title, url){
    	if ($('#tt').tabs('exists', title)){
    		$('#tt').tabs('select', title);
    	} else {
    		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
    		$('#tt').tabs('add',{
    			title:title,
    			content:content,
    			closable:true
    		});
    	}
    }
    </script>
</body>
</html>