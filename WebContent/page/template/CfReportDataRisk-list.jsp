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
<title>CfReportDataRisk管理</title>
</head>
<body>
<%@include file="/commons/statics.jsp"%>
	<!-- 搜索模块 -->
	<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
		<form id="serachFrom" method="post">
			<table style="width: 100%;">
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>outItemCode:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="outItemCode" name="outItemCode" class="easyui-textbox" data-options="prompt: 'outItemCode'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>comCode:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="comCode" name="comCode" class="easyui-textbox" data-options="prompt: 'comCode'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>outItemType:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="outItemType" name="outItemType" class="easyui-textbox" data-options="prompt: 'outItemType'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>outItemCodeName:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="outItemCodeName" name="outItemCodeName" class="easyui-textbox" data-options="prompt: 'outItemCodeName'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>reportItemCode:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportItemCode" name="reportItemCode" class="easyui-textbox" data-options="prompt: 'reportItemCode'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>reportItemValue:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportItemValue" name="reportItemValue" class="easyui-textbox" data-options="prompt: 'reportItemValue'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>reportItemWanValue:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportItemWanValue" name="reportItemWanValue" class="easyui-textbox" data-options="prompt: 'reportItemWanValue'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>textValue:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="textValue" name="textValue" class="easyui-textbox" data-options="prompt: 'textValue'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>desText:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="desText" name="desText" class="easyui-textbox" data-options="prompt: 'desText'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>fileText:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="fileText" name="fileText" class="easyui-textbox" data-options="prompt: 'fileText'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>month:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="month" name="month" class="easyui-textbox" data-options="prompt: 'month'"  style="width:60%;">
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
		    				<label>source:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="source" name="source" class="easyui-textbox" data-options="prompt: 'source'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>reportType:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportType" name="reportType" class="easyui-textbox" data-options="prompt: 'reportType'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>reportRate:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportRate" name="reportRate" class="easyui-textbox" data-options="prompt: 'reportRate'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>computeLevel:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="computeLevel" name="computeLevel" class="easyui-textbox" data-options="prompt: 'computeLevel'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>reportItemValueSource:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportItemValueSource" name="reportItemValueSource" class="easyui-textbox" data-options="prompt: 'reportItemValueSource'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>operator:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="operator" name="operator" class="easyui-textbox" data-options="prompt: 'operator'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>operDate:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="operDate" name="operDate" class="easyui-textbox" data-options="prompt: 'operDate'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>temp:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="temp" name="temp" class="easyui-textbox" data-options="prompt: 'temp'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>reportId:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportId" name="reportId" class="easyui-textbox" data-options="prompt: 'reportId'"  style="width:60%;">
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
			url=${pageContext.request.contextPath}/cfReportDataRisk/listpage.do
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
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCfReportDataRisk()">新建</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editCfReportDataRisk()">编辑</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="destroyCfReportDataRisk()">删除</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="outItemCode" width="25" sortable="true">outItemCode</th>
					<th field="comCode" width="25" sortable="true">comCode</th>
					<th field="outItemType" width="25" sortable="true">outItemType</th>
					<th field="outItemCodeName" width="25" sortable="true">outItemCodeName</th>
					<th field="reportItemCode" width="25" sortable="true">reportItemCode</th>
					<th field="reportItemValue" width="25" sortable="true">reportItemValue</th>
					<th field="reportItemWanValue" width="25" sortable="true">reportItemWanValue</th>
					<th field="textValue" width="25" sortable="true">textValue</th>
					<th field="desText" width="25" sortable="true">desText</th>
					<th field="fileText" width="25" sortable="true">fileText</th>
					<th field="month" width="25" sortable="true">month</th>
					<th field="quarter" width="25" sortable="true">quarter</th>
					<th field="source" width="25" sortable="true">source</th>
					<th field="reportType" width="25" sortable="true">reportType</th>
					<th field="reportRate" width="25" sortable="true">reportRate</th>
					<th field="computeLevel" width="25" sortable="true">computeLevel</th>
					<th field="reportItemValueSource" width="25" sortable="true">reportItemValueSource</th>
					<th field="operator" width="25" sortable="true">operator</th>
					<th field="operDate" width="25" sortable="true">operDate</th>
					<th field="temp" width="25" sortable="true">temp</th>
					<th field="reportId" width="25" sortable="true">reportId</th>
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
	    				<label>outItemCode:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="outItemCode" type="text" name="outItemCode" class="easyui-textbox" data-options="prompt: 'outItemCode'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>comCode:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="comCode" type="text" name="comCode" class="easyui-textbox" data-options="prompt: 'comCode'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>outItemType:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="outItemType" type="text" name="outItemType" class="easyui-textbox" data-options="prompt: 'outItemType'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>outItemCodeName:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="outItemCodeName" type="text" name="outItemCodeName" class="easyui-textbox" data-options="prompt: 'outItemCodeName'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>reportItemCode:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportItemCode" type="text" name="reportItemCode" class="easyui-textbox" data-options="prompt: 'reportItemCode'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>reportItemValue:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportItemValue" type="text" name="reportItemValue" class="easyui-textbox" data-options="prompt: 'reportItemValue'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>reportItemWanValue:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportItemWanValue" type="text" name="reportItemWanValue" class="easyui-textbox" data-options="prompt: 'reportItemWanValue'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>textValue:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="textValue" type="text" name="textValue" class="easyui-textbox" data-options="prompt: 'textValue'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>desText:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="desText" type="text" name="desText" class="easyui-textbox" data-options="prompt: 'desText'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>fileText:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="fileText" type="text" name="fileText" class="easyui-textbox" data-options="prompt: 'fileText'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>month:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="month" type="text" name="month" class="easyui-textbox" data-options="prompt: 'month'"  style="width:60%;">
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
	    				<label>source:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="source" type="text" name="source" class="easyui-textbox" data-options="prompt: 'source'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>reportType:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportType" type="text" name="reportType" class="easyui-textbox" data-options="prompt: 'reportType'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>reportRate:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportRate" type="text" name="reportRate" class="easyui-textbox" data-options="prompt: 'reportRate'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>computeLevel:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="computeLevel" type="text" name="computeLevel" class="easyui-textbox" data-options="prompt: 'computeLevel'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>reportItemValueSource:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportItemValueSource" type="text" name="reportItemValueSource" class="easyui-textbox" data-options="prompt: 'reportItemValueSource'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>operator:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="operator" type="text" name="operator" class="easyui-textbox" data-options="prompt: 'operator'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>operDate:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="operDate" type="text" name="operDate" class="easyui-textbox" data-options="prompt: 'operDate'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>temp:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="temp" type="text" name="temp" class="easyui-textbox" data-options="prompt: 'temp'"  style="width:60%;">
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
	    	</table>
	    	<input type="hidden" name="id" value="000"/>
		</form>
		<div id="dlg-buttons">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveCfReportDataRisk()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	    </div>
    </div>
</body>
<script  type="text/javascript">
// 新建
    function newCfReportDataRisk(){
    	$('#dlg').dialog('open').dialog('setTitle','新增');
    	$('#fm').form('clear');
    	url='${pageContext.request.contextPath}/cfReportDataRisk/add.do';
	}
//保存
function saveCfReportDataRisk(){
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
function editCfReportDataRisk(){
    var rows = $('#dg').datagrid('getSelections');
    if(!rows){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','编辑');
	$('#fm').form('load',rows[0]);
	url = '${pageContext.request.contextPath}/cfReportDataRisk/update.do?id='+rows[0].id;
}

//删除
function destroyCfReportDataRisk(){
	var rows = $('#dg').datagrid('getSelections');
	var idArr = new Array();
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].id;
	}
	var data =[{name:'ids',value:idArr.join(',') }];
	if(rows.length==0){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
		$.messager.confirm('删除','确认删除吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/cfReportDataRisk/delete.do',data,function(result){
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