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
<title>CalCfReportRelation管理</title>
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
		    				<label>指标代码:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="portItemCode" name="portItemCode" class="easyui-textbox" data-options="prompt: '指标代码'" >
		    			</td>
			    	<td style="width:9%;text-align: right;" >
		    				<label>指标类型:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="portItemType" name="portItemType" class="easyui-textbox" data-options="prompt: '指标类型'"  >
		    			</td>				    
					    <td style="width:8%;text-align: right;" >
		    				<label>接口类型:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="interfaceType" name="interfaceType" class="easyui-textbox" data-options="prompt: '接口类型'"  >
		    			</td>
			    	<td style="width:8%;text-align: right;" >
		    				<label>接口优先级:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="interfacePriority" name="interfacePriority" class="easyui-textbox" data-options="prompt: '接口优先级'"  >
		    			</td>
				    </tr>
		    		<tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>接口取数规则:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="interfaceGetData" name="interfaceGetData" class="easyui-textbox" data-options="prompt: '接口取数规则'"  >
		    			</td>
			    		<td style="width:8%;text-align: right;" >
		    				<label>计算规则类型:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="calculateType" name="calculateType" class="easyui-textbox" data-options="prompt: '计算规则类型'"  >
		    			</td>
					    <td style="width:8%;text-align: right;" >
		    				<label>计算规则:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="calculateRule" name="calculateRule" class="easyui-textbox" data-options="prompt: '计算规则'"  >
		    			</td>
			    	<td style="width:8%;text-align: right;" >
		    				<label>是否需要参数:</label>
		    			</td>
		    			<td style="width:13%;">
			    			<select style="width:100%;" id="varFlag" name="varFlag" class="easyui-combobox" data-options="prompt: '是否完成手工导入'"  editable="false" >
			    					<option value=""></option>
			    					<option value="0">否</option>
			    					<option value="1">是</option>
			    			</select>
		    			</td>
		    		</tr>
		    		<tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>计算参数:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="computeVar" name="computeVar" class="easyui-textbox" data-options="prompt: '计算参数'"  >
		    			</td>
			    	<td style="width:8%;text-align: right;" >
		    				<label>计算参数取数规则:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="computeVarRule" name="computeVarRule" class="easyui-textbox" data-options="prompt: '计算参数取数规则'"  >
		    			</td>
					    <td style="width:8%;text-align: right;" >
		    				<label>备注:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="remark" name="remark" class="easyui-textbox" data-options="prompt: '备注'"  >
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
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCalCfReportRelation()">新建</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editCalCfReportRelation()">编辑</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="destroyCalCfReportRelation()">删除</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="portItemCode" width="11%" align="center" halign="center" sortable="true">指标代码</th>
					<th field="portItemType" width="5%" align="center" halign="center" sortable="true">指标类型</th>
					<th field="interfaceType" width="6%" align="center" halign="center" sortable="true">接口类型</th>
					<th field="interfacePriority" width="7%"align="center" halign="center"  sortable="true">接口优先级</th>
					<th field="interfaceGetData" width="18%" align="left" halign="center" sortable="true">接口取数规则</th>
					<th field="calculateType" width="7%" align="center" halign="center" sortable="true">计算规则类型</th>
					<th field="calculateRule" width="9%" align="left" halign="center" sortable="true">计算规则</th>
					<th field="varFlag" width="8%" align="center" halign="center" sortable="true" formatter="isYON">是否需要参数</th>
					<th field="computeVar" width="11%" align="left" halign="center" sortable="true">计算参数</th>
					<th field="computeVarRule" width="14%" align="left" halign="center" sortable="true">计算参数取数规则</th>
					<th field="remark" width="9%" align="center" halign="center" sortable="true" hidden>备注</th>
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
	    				<label>指标代码:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="portItemCodeOne" type="text" name="portItemCode" class="easyui-textbox"data-options="prompt:'用户代码',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			<td style="width:10%;" >
	    				<label>指标类型:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="portItemType" type="text" name="portItemType" class="easyui-textbox"data-options="prompt:'指标类型',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>指标归属:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="portItemHome" type="text" name="portItemHome" class="easyui-textbox"data-options="prompt:'指标归属',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    			<td style="width:10%;" >
	    				<label>接口类型:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="interfaceType" type="text" name="interfaceType" class="easyui-textbox"data-options="prompt:'接口类型',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>接口优先级:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="interfacePriority" type="text" name="interfacePriority" class="easyui-textbox"data-options="prompt:'接口优先级',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    			<td style="width:10%;" >
	    				<label>接口取数规则:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="interfaceGetData" type="text" name="interfaceGetData" class="easyui-textbox"data-options="prompt:'接口取数规则',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
	    		<tr>
				    <td style="width:10%;" >
	    				<label>接口取数规则参数:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="nterfaceGetDataVar" type="text" name="nterfaceGetDataVar" class="easyui-textbox"data-options="prompt:'接口取数规则参数',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    			<td style="width:10%;" >
	    				<label>计算规则类型:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="calculateType" type="text" name="calculateType" class="easyui-textbox"data-options="prompt:'计算规则类型',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>计算规则:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="calculateRule" type="text" name="calculateRule" class="easyui-textbox"data-options="prompt:'计算规则',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    			<td style="width:10%;" >
	    				<label>是否需要参数:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="varFlag" type="text" name="varFlag" class="easyui-textbox"data-options="prompt:'是否需要参数',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>计算参数:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="computeVar" type="text" name="computeVar" class="easyui-textbox"data-options="prompt:'计算参数',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    			<td style="width:10%;" >
	    				<label>计算参数取数规则:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="computeVarRule" type="text" name="computeVarRule" class="easyui-textbox"data-options="prompt:'计算参数取数规则',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>备注:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="remark" type="text" name="remark" class="easyui-textbox" data-options="prompt: '备注'"  style="width:60%;">
	    			</td>
	    		</tr>
	    	</table>
	    	<input type="hidden" name="id" value="000"/>
		</form>
		<div id="dlg-buttons">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveCalCfReportRelation()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	    </div>
    </div>
</body>
<script  type="text/javascript">

$(function(){
	serach();
})
function isYON(value){
	if(value=='0'){
		return '否';
	}else{
		return '是';
		} 
}
// 新建
    function newCalCfReportRelation(){
    	$('#dlg').dialog('open').dialog('setTitle','新增');
    	$('#fm').form('clear');
    	url='${pageContext.request.contextPath}/calCfReportRelation/add.do';
	}
//保存
function saveCalCfReportRelation(){
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
function editCalCfReportRelation(){
    var rows = $('#dg').datagrid('getSelections');
    if(!rows){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','编辑');
	$('#portItemCodeOne').combo('readonly',true);
	$('#fm').form('load',rows[0]);
	url = '${pageContext.request.contextPath}/calCfReportRelation/update.do?id='+rows[0].portItemCode;
}

//删除
function destroyCalCfReportRelation(){
	var rows = $('#dg').datagrid('getSelections');
	var idArr = new Array();
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].portItemCode;
	}
	var data =[{name:'ids',value:idArr.join(',') }];
	if(rows.length==0){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
		$.messager.confirm('删除','确认删除吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/calCfReportRelation/delete.do',data,function(result){
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
		url:'${pageContext.request.contextPath}/calCfReportRelation/listpage.do',
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