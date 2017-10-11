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
<title>Kdefine管理</title>
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
		<form id="serachFrom">
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">
		    		<tr>
					    <td style="width:9%;text-align: right;" >
		    				<label>K代码:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="kcode" name="kcode" class="easyui-textbox" data-options="prompt: 'K代码'"  style="width:100%;">
		    			</td>
			    	<td style="width:9%;text-align: right;" >
		    				<label>风险类型代码:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="risktype1Code" name="risktype1Code" class="easyui-textbox" data-options="prompt: '风险类型代码'"  style="width:100%;">
		    			</td>		    
					    <td style="width:9%;text-align: right;" >
		    				<label>风险类型名称:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="risktype1Name" name="risktype1Name" class="easyui-textbox" data-options="prompt: '风险类型名称'"  style="width:100%;">
		    			</td>
			    	<td style="width:9%;text-align: right;" >
		    				<label>风险一级明细代码:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="risktype2Code" name="risktype2Code" class="easyui-textbox" data-options="prompt: '风险一级明细代码'"  style="width:100%;">
		    			</td>
				    </tr>
		    		<tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>风险一级明细名称:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="risktype2Name" name="risktype2Name" class="easyui-textbox" data-options="prompt: '风险一级明细名称'"  style="width:100%;">
		    			</td>
			    	<td style="width:8%;text-align: right;" >
		    				<label>风险二级明细代码:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="risktype3Code" name="risktype3Code" class="easyui-textbox" data-options="prompt: '风险二级明细代码'"  style="width:100%;">
		    			</td>
					    <td style="width:8%;text-align: right;" >
		    				<label>风险二级明细名称:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="risktype3Name" name="risktype3Name" class="easyui-textbox" data-options="prompt: '风险二级明细名称'"  style="width:100%;">
		    			</td>
			    	<td style="width:8%;text-align: right;" >
		    				<label>业务类型代码:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="detailCode" name="detailCode" class="easyui-textbox" data-options="prompt: '业务类型代码'"  style="width:100%;">
		    			</td>
				    </tr>
		    		<tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>业务类型名称:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="detailDescribe" name="detailDescribe" class="easyui-textbox" data-options="prompt: '业务类型名称'"  style="width:100%;">
		    			</td>
			    	<td style="width:8%;text-align: right;" >
		    				<label>描述:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="kdefine" name="kdefine" class="easyui-textbox" data-options="prompt: '描述'"  style="width:100%;">
		    			</td>
					    <td style="width:8%;text-align: right;" >
		    				<label>k(From):</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="kdefineFrom" name="kdefineFrom" class="easyui-textbox" data-options="prompt: 'k(From)'"  style="width:100%;">
		    			</td>
			    	<td style="width:8%;text-align: right;" >
		    				<label>k(To):</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="kdefineTo" name="kdefineTo" class="easyui-textbox" data-options="prompt: 'k(To)'"  style="width:100%;">
		    			</td>
				    </tr>
				    <tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>是否需要计算:</label>
		    			</td>
		    			<td style="width:13%;">
			    			<select class="easyui-combobox" style="width: 100%;" id="kValueRule" name="kValueRule" data-options="prompt: '是否需要计算',editable:false">
								<option value=""></option>
								<option value="0">否</option>
								<option value="1">是</option>
							</select>
		    			</td>
			    	<td style="width:8%;text-align: right;" >
		    				<label>决定k参数:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="kDefineVar" name="kDefineVar" class="easyui-textbox" data-options="prompt: '决定k参数'"  style="width:100%;">
		    			</td>
					    <td style="width:8%;text-align: right;" >
		    				<label>k:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="kvalue" name="kvalue" class="easyui-textbox" data-options="prompt: 'k'"  style="width:100%;">
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
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newKdefine()">新建</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editKdefine()">编辑</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="destroyKdefine()">删除</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="kcode" width="14%" align="center" halign="center" sortable="true">K代码</th>
					<th field="risktype1Code" width="7%" align="center" halign="center" sortable="true">风险类型</br>代码</th>
					<th field="risktype1Name" width="6%" align="center" halign="center" sortable="true">风险类型</br>名称</th>
					<th field="risktype2Code" width="7%" align="center" halign="center" sortable="true">风险类型</br>名称</th>
					<th field="risktype2Name" width="7%" align="center" halign="center" sortable="true">风险一级</br>明细名称</th>
					<th field="risktype3Code" width="7%" align="center" halign="center" sortable="true">风险二级</br>明细代码</th>
					<th field="risktype3Name" width="6%"  align="center" halign="center" sortable="true">风险二级</br>明细名称</th>
					<th field="detailCode" width="7%" align="center" halign="center" sortable="true">业务类型</br>代码</th>
					<th field="detailDescribe" width="6%" align="center" halign="center" sortable="true">业务类型</br>名称</th>
					<th field="kdefine" width="12%" align="left" halign="center" sortable="true">描述</th>
					<th field="kdefineFrom" width="5%" align="center" halign="center" sortable="true">k(From)</th>
					<th field="kdefineTo" width="7%" align="center" halign="center" sortable="true">k(To)</th>
					<th field="kvalue" width="5%" align="center" halign="center" sortable="true">k</th>
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
	    				<label>K代码:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="kcode" type="text" name="kcode" class="easyui-textbox"data-options="prompt:'K代码',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			 <td style="width:10%;" >
	    				<label>风险类型代码:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="risktype1Code" type="text" name="risktype1Code" class="easyui-textbox"data-options="prompt:'风险类型代码',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>风险类型名称:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="risktype1Name" type="text" name="risktype1Name"class="easyui-textbox"data-options="prompt:'用户代码',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			 <td style="width:10%;" >
	    				<label>风险一级明细代码:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="risktype2Code" type="text" name="risktype2Code" class="easyui-textbox"data-options="prompt:'风险一级明细代码',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>风险一级明细名称:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="risktype2Name" type="text" name="risktype2Name" class="easyui-textbox"data-options="prompt:'风险一级明细名称',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			 <td style="width:10%;" >
	    				<label>风险二级明细代码:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="risktype3Code" type="text" name="risktype3Code" class="easyui-textbox"data-options="prompt:'风险二级明细代码',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>风险二级明细名称:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="risktype3Name" type="text" name="risktype3Name" class="easyui-textbox"data-options="prompt:'风险二级明细名称',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			  <td style="width:10%;" >
	    				<label>业务类型代码:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="detailCode" type="text" name="detailCode" class="easyui-textbox"data-options="prompt:'业务类型代码',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>业务类型名称:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="detailDescribe" type="text" name="detailDescribe" class="easyui-textbox"data-options="prompt:'业务类型名称',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			 <td style="width:10%;" >
	    				<label>描述:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="kdefine" type="text" name="kdefine" class="easyui-textbox"data-options="prompt:'描述',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>k(From):</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="kdefineFrom" type="text" name="kdefineFrom" class="easyui-textbox"data-options="prompt:'k(From)',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    			 <td style="width:10%;" >
	    				<label>k(To):</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="kdefineTo" type="text" name="kdefineTo"class="easyui-textbox"data-options="prompt:'k(To)',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
				
				<tr>
				    <td style="width:10%;" >
	    				<label>k:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="kvalue" type="text" name="kvalue" class="easyui-textbox"data-options="prompt:'k',required:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
	    			</td>
	    		</tr>
	    	</table>
	    	<input type="hidden" name="id" value="0"/>
		</form>
		<div id="dlg-buttons">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveKdefine()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	    </div>
    </div>
</body>
<script  type="text/javascript">
$(function(){
	serach();
})
// 新建
    function newKdefine(){
    	$('#dlg').dialog('open').dialog('setTitle','新增');
    	$('#fm').form('clear');
    	url='${pageContext.request.contextPath}/kdefine/add.do';
	}
//保存
function saveKdefine(){
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
function editKdefine(){
    var rows = $('#dg').datagrid('getSelections');
    if(!rows){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','编辑');
	$('#fm').form('load',rows[0]);
	url = '${pageContext.request.contextPath}/kdefine/update.do?id='+rows[0].id;
}

//删除
function destroyKdefine(){
	var rows = $('#dg').datagrid('getSelections');
	var idArr = new Array();
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].id;
	}
	var data =[{name:'ids',value:idArr.join(',') }];
	if(rows.length==0){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
		$.messager.confirm('删除','确认删除吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/kdefine/delete.do',data,function(result){
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
		url:'${pageContext.request.contextPath}/kdefine/listpage.do',
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