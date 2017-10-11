<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>案例样式</title>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
	<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
		<form id="serachFrom" method="post">
			<table style="width: 100%;">
	    		<tr height="30">
	    			<td style="width:10%;" >
	    				<label>角色代码:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="contractId" name="roleCode" class="easyui-textbox" data-options="prompt: '角色代码'"  style="width:60%;">
	    			</td>
	    			<td style="width:10%;" >
	    				<label>角色名称:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="contractName"  name="roleName" class="easyui-textbox" data-options="prompt: '角色名称'"  style="width:60%;">
	    			</td>
	    			<td style="width:10%;" >
	    				<span  style="width:10%;" ></span>
	    			</td>
	    			<td style="width:30%;">
	    				<span  style="width:10%;" ></span>
	    			</td>
	    		</tr>
	    		<tr height="30">
	    			<td style="width:10%;" >
	    				<label>开始时间:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="contractId" name="roleCode" class="easyui-datebox" data-options="prompt: '开始时间',validType:'md[\'2015-12-25\']',editable:true"  style="width:60%;">
	    			</td>
	    			<td style="width:10%;" >
	    				<label>结束时间:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="contractName"  name="roleName" class="easyui-datebox" data-options="prompt: '结束时间',validType:'md[\'2015-12-25\']',editable:true"  style="width:60%;">
	    			</td>
	    			<td style="width:10%;" >
	    				<span  style="width:10%;" ></span>
	    			</td>
	    			<td style="width:30%;">
	    				<span  style="width:10%;" ></span>
	    			</td>
	    		</tr>
	    		<tr height="30">
	    			<td style="width:10%;" >
	    				<label>备注:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="remark" name="remark" class="easyui-textbox" data-options="prompt: '备注'"  style="width:60%;">
	    			</td>
	    			<td style="width:10%;" >
	    				<span  style="width:10%;" ></span>
	    			</td>
	    			<td style="width:30%;">
	    				<span  style="width:10%;" ></span>
	    			</td>
	    			<td style="width:40%;" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">搜索</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
	    			</td>
	    		</tr>
	    	</table>
		</form>
	</div>
	<div style="margin:20px 0;">
		<a href="#" class="easyui-linkbutton" onclick="message1()">提示信息1</a>
		<a href="#" class="easyui-linkbutton" onclick="message2()">提示信息2</a>
		<a href="#" class="easyui-linkbutton" onclick="message3()">提示信息3</a>
		<a href="#" class="easyui-linkbutton" onclick="message4()">提示信息4</a>
	</div>
	<table class="easyui-datagrid" title="datagrid">
		<thead>
			<tr>
				<th data-options="field:'menuName',width:'15%'">菜单名称</th>
				<th data-options="field:'id',width:'15%'" >id</th>
				<th data-options="field:'menuCode',width:'15%'">菜单编码</th>
				<th data-options="field:'menuIcon',width:'15%'">菜单图片</th>
				<th data-options="field:'script',width:'15%'">菜单URL</th>
				<th data-options="field:'remark',width:'15%'">菜单描述</th>
			</tr>
		</thead>
	</table>
    <script type="text/javascript">
  	//editable:false设置不能手动输入
    //覆盖easyui默认的校验格式，校验时间的输入格式。
    $.extend($.fn.validatebox.defaults.rules, {
		md: {
			validator: function(value, param){
				/* var d1 = $.fn.datebox.defaults.parser(param[0]);
				va r d2 = $.fn.datebox.defaults.parser(value);*/
				//var d3=$(this).datebox('getValue');
				//console.log(value+param);
				return /^\d{4}-\d{2}-\d{2}$/.test(value);
			},
			message: '时间格式必须按照这样来写 {0}.'
		}
	});
    function reset(){
    	$('#serachFrom').form('clear');
    }
	function message1(){
		jsutil.msg.alert('提示');
  	}
	function message2(){
		jsutil.msg.info('上传成功');
  	}
	function message3(){
		jsutil.msg.warn('警告');
  	}
	function message4(){
		jsutil.msg.err('错误');
  	}
    </script>
</body>
</html>