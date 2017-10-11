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
<title>RhoMatrixDefine管理</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
</head>
<body>
<%@include file="/commons/statics.jsp"%>
	<!-- 搜索模块 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form id="serachFrom" method="post">
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">
		    		<tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>相关系数矩阵代码:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="rhomatrixCode" name="rhomatrixCode" class="easyui-textbox" data-options="prompt: '相关系数矩阵代码'"  >
		    			</td>
			    	<td style="width:8%;text-align: right;" >
		    				<label>相关系数矩阵名称:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="rhomatrixName" name="rhomatrixName" class="easyui-textbox" data-options="prompt: '相关系数矩阵名称'"  >
		    			</td>				    
					    <td style="width:7%;text-align: right;" >
		    				<label>指标因子代码:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="reportitemcode" name="reportitemcode" class="easyui-textbox" data-options="prompt: '指标因子代码'"  >
		    			</td>
			    		<td style="width:6%;text-align: right;" >
		    				<label>行列:</label>
		    			</td>
		    			<td style="width:12%;">
		    				<input style="width:100%;" id="columnLine" name="columnLine" class="easyui-textbox" data-options="prompt: '行列'" >
		    			</td>
				    
		    		<tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>相关系数矩阵数据:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="rhomatrixValue" name="rhomatrixValue" class="easyui-textbox" data-options="prompt: '相关系数矩阵数据'"  >
		    			</td>
			    		<td style="width:8%;text-align: right;" >
		    				<label>备用字段:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="remark" name="remark" class="easyui-textbox" data-options="prompt: '备用字段'"  >
		    			</td>
		    			<td colspan="4" style="width:42%;">
		    			</td>				    
		    		</tr>
		    		<tr>
	    				<td colspan="8" style="text-align: right; padding-right: 20px" >
		    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">搜索</a>
		    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:8%;">重置</a>
		    			</td>
	    			</tr>
	    	</table>
		</form>
	</div>
<!-- 	操作，展示部分 -->
	<div  class="easyui-panel" style="height: auto; width:100%; overflow: hidden;">
		<table id="dg" class="easyui-datagrid"
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="false" 
			pagination="true" 
			striped="true"
			nowrap="false"
			autoRowHeight="false"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newRhoMatrixDefine()">新建</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editRhoMatrixDefine()">编辑</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="destroyRhoMatrixDefine()">删除</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="rhomatrixCode" width="15%" align="left" halign="center" sortable="true">相关系数矩阵代码</th>
					<th field="rhomatrixName" width="31%" align="left" halign="center" sortable="true">相关系数矩阵名称</th>
					<th field="reportitemcode" width="14%" align="center" halign="center" sortable="true">指标因子代码</th>
					<th field="columnLine" width="5%" align="center" halign="center" sortable="true">行列</th>
					<th field="rhomatrixValue" width="30%" align="left" halign="center" sortable="true">相关系数矩阵数据</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--  新增弹框界面-->
		<div id="dlg" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons">

		<form id="fm" method="post">
			<table cellpadding="5">
				<tr>
				    <td style="width:10%;" >
	    				<label>相关系数矩阵代码:</label>
	    			</td>
	    			<td style="width:20%;">
	    				<input id="rhomatrixCodeOne" type="text" name="rhomatrixCode" class="easyui-textbox"data-options="prompt:'相关系数矩阵代码',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			<td style="width:10%;" >
	    				<label>相关系数矩阵名称:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="rhomatrixName" type="text" name="rhomatrixName" class="easyui-textbox"data-options="prompt:'相关系数矩阵名称',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>指标因子代码:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportitemcode" type="text" name="reportitemcode" class="easyui-textbox"data-options="prompt:'指标因子代码',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			<td style="width:10%;" >
	    				<label>行列:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="columnLine" type="text" name="columnLine" class="easyui-textbox"data-options="prompt:'行列',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>相关系数矩阵数据:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="rhomatrixValue" type="text" name="rhomatrixValue" class="easyui-textbox"data-options="prompt:'相关系数矩阵数据',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			 <td style="width:10%;" >
	    				<label>备用字段:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="remark" type="text" name="remark"class="easyui-textbox"data-options="prompt:'备用字段',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
	    	</table>
	    	<input type="hidden" name="id" value="0"/>
		</form>
		<div id="dlg-buttons">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRhoMatrixDefine()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	    </div>
    </div>
</body>
<script  type="text/javascript">
//初始化
$(function(){
	serach();
})

// 新建
    function newRhoMatrixDefine(){
    	$('#dlg').dialog('open').dialog('setTitle','新增');
    	$('#fm').form('clear');
    	
    	url='${pageContext.request.contextPath}/rhoMatrixDefine/add.do';
	}
//保存
function saveRhoMatrixDefine(){
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
function editRhoMatrixDefine(){
    var rows = $('#dg').datagrid('getSelections');
    if(!rows){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','编辑');	
	$('#rhomatrixCodeOne').combo('readonly',true);
	$('#fm').form('load',rows[0]);
	url = '${pageContext.request.contextPath}/rhoMatrixDefine/update.do?id='+rows[0].rhomatrixCode;		
}

//删除
function destroyRhoMatrixDefine(){
	var rows = $('#dg').datagrid('getSelections');
	var idArr = new Array();
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].rhomatrixCode;
	}
	var data =[{name:'ids',value:idArr.join(',') }];
	if(rows.length==0){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
		$.messager.confirm('删除','确认删除吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/rhoMatrixDefine/delete.do',data,function(result){
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
	//$('#dg').datagrid("load", params);
	$("#dg").datagrid({
		url:'${pageContext.request.contextPath}/rhoMatrixDefine/listpage.do',
		queryParams: params,
		onBeforeLoad: function(){
			if(!$('#serachFrom').form('validate')) return false;
		},
		onLoadSuccess: function(data){
			if(data.total>0) {
 				return  false;
 			}
 			$.messager.alert("提示","未查询到相关数据","info");
		}
	});
}

</script>
</html>