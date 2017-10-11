<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String path=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); 
%>
<title>InterTabFinaDateInfo管理</title>
</head>
<body>
<%@include file="/commons/statics.jsp"%>
	<!-- 搜索模块 -->
	<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
		<form id="serachFrom" method="post">
			<table style="width: 100%;">
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>year:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="year" name="year" class="easyui-textbox" data-options="prompt: 'year'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>quarter:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="quarter" name="quarter" class="easyui-textbox" data-options="prompt: 'quarter'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>reportDate:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportDate" name="reportDate" class="easyui-textbox" data-options="prompt: 'reportDate'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>reportId:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportId" name="reportId" class="easyui-textbox" data-options="prompt: 'reportId'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>accountType:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="accountType" name="accountType" class="easyui-textbox" data-options="prompt: 'accountType'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>properCode:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="properCode" name="properCode" class="easyui-textbox" data-options="prompt: 'properCode'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>properName:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="properName" name="properName" class="easyui-textbox" data-options="prompt: 'properName'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>startDate:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="startDate" name="startDate" class="easyui-textbox" data-options="prompt: 'startDate'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>endDate:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="endDate" name="endDate" class="easyui-textbox" data-options="prompt: 'endDate'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>currDate:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="currDate" name="currDate" class="easyui-textbox" data-options="prompt: 'currDate'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>temp1:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="temp1" name="temp1" class="easyui-textbox" data-options="prompt: 'temp1'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>temp2:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="temp2" name="temp2" class="easyui-textbox" data-options="prompt: 'temp2'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>temp3:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="temp3" name="temp3" class="easyui-textbox" data-options="prompt: 'temp3'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>temp4:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="temp4" name="temp4" class="easyui-textbox" data-options="prompt: 'temp4'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>temp5:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="temp5" name="temp5" class="easyui-textbox" data-options="prompt: 'temp5'"  style="width:60%;">
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
<!-- 	操作，展示部分 -->
	<div>
		<table id="dg" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/interTabFinaDateInfo/listpage.do
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="false" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newInterTabFinaDateInfo()">新建</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInterTabFinaDateInfo()">编辑</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="destroyInterTabFinaDateInfo()">删除</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="year" width="25" sortable="true">year</th>
					<th field="quarter" width="25" sortable="true">quarter</th>
					<th field="reportDate" width="25" sortable="true">reportDate</th>
					<th field="reportId" width="25" sortable="true">reportId</th>
					<th field="accountType" width="25" sortable="true">accountType</th>
					<th field="properCode" width="25" sortable="true">properCode</th>
					<th field="properName" width="25" sortable="true">properName</th>
					<th field="startDate" width="25" sortable="true">startDate</th>
					<th field="endDate" width="25" sortable="true">endDate</th>
					<th field="currDate" width="25" sortable="true">currDate</th>
					<th field="temp1" width="25" sortable="true">temp1</th>
					<th field="temp2" width="25" sortable="true">temp2</th>
					<th field="temp3" width="25" sortable="true">temp3</th>
					<th field="temp4" width="25" sortable="true">temp4</th>
					<th field="temp5" width="25" sortable="true">temp5</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog"
		style="width: 400px; height: 280px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
				<tr>
				    <td style="width:10%;" >
	    				<label>year:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="year" type="text" name="year" class="easyui-textbox" data-options="prompt: 'year'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>quarter:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="quarter" type="text" name="quarter" class="easyui-textbox" data-options="prompt: 'quarter'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>reportDate:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportDate" type="text" name="reportDate" class="easyui-textbox" data-options="prompt: 'reportDate'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>reportId:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportId" type="text" name="reportId" class="easyui-textbox" data-options="prompt: 'reportId'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>accountType:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="accountType" type="text" name="accountType" class="easyui-textbox" data-options="prompt: 'accountType'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>properCode:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="properCode" type="text" name="properCode" class="easyui-textbox" data-options="prompt: 'properCode'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>properName:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="properName" type="text" name="properName" class="easyui-textbox" data-options="prompt: 'properName'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>startDate:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="startDate" type="text" name="startDate" class="easyui-textbox" data-options="prompt: 'startDate'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>endDate:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="endDate" type="text" name="endDate" class="easyui-textbox" data-options="prompt: 'endDate'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>currDate:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="currDate" type="text" name="currDate" class="easyui-textbox" data-options="prompt: 'currDate'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>temp1:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="temp1" type="text" name="temp1" class="easyui-textbox" data-options="prompt: 'temp1'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>temp2:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="temp2" type="text" name="temp2" class="easyui-textbox" data-options="prompt: 'temp2'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>temp3:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="temp3" type="text" name="temp3" class="easyui-textbox" data-options="prompt: 'temp3'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>temp4:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="temp4" type="text" name="temp4" class="easyui-textbox" data-options="prompt: 'temp4'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>temp5:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="temp5" type="text" name="temp5" class="easyui-textbox" data-options="prompt: 'temp5'"  style="width:60%;">
	    			</td>
	    		</tr>
	    	</table>
	    	<input type="hidden" name="id" value="000"/>
		</form>
		<div id="dlg-buttons">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveInterTabFinaDateInfo()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	    </div>
    </div>
</body>
<script  type="text/javascript">
// 新建
    function newInterTabFinaDateInfo(){
    	$('#dlg').dialog('open').dialog('setTitle','新增');
    	$('#fm').form('clear');
    	url='${pageContext.request.contextPath}/interTabFinaDateInfo/add.do';
	}
//保存
function saveInterTabFinaDateInfo(){
	$('#fm').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			var result = eval('('+result+')');
			if (result.errorMsg){
				jsutil.msg.alert(result.errorMsg);
			} else {
				$('#dlg').dialog('close');		// close the dialog
				$('#dg').datagrid('reload');	// reload the user data
				jsutil.msg.alert("保存成功");
			}
		}
	});
}
//修改
function editInterTabFinaDateInfo(){
    var rows = $('#dg').datagrid('getSelections');
    if(!rows){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','编辑');
	$('#fm').form('load',rows[0]);
	url = '${pageContext.request.contextPath}/interTabFinaDateInfo/update.do?id='+rows[0].id;
}

//删除
function destroyInterTabFinaDateInfo(){
	var rows = $('#dg').datagrid('getSelections');
	var idArr = new Array();
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].id;
	}
	var data =[{name:'ids',value:idArr.join(',') }];
	if(rows.length==0){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
		$.messager.confirm('删除','确认删除吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/interTabFinaDateInfo/delete.do',data,function(result){
					if (result.success){
						$('#dg').datagrid('reload');	// reload the user data
						jsutil.msg.alert("删除成功");
					} else {
						jsutil.msg.alert(result.errorMsg);
					}
				},'json');
			}
		});
}
//	重置
function reset(){
	$('#serachFrom').form("clear");
}


//搜索
function serach(){
	var params = {};
	$('#serachFrom').find('input').each(function(){
        var obj = $(this);
        var name = obj.attr('name');
        if(name){
            params[name] = obj.val();
        }
    });
	$('#dg').datagrid("load", params);
}
</script>
</html>