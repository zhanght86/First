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
<title>CfReportRowAddDataRisk管理</title>
</head>
<body>
<%@include file="/commons/statics.jsp"%>
	<!-- 搜索模块 -->
	<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
		<form id="serachFrom" method="post">
			<table style="width: 100%;">
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>tableCode:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="tableCode" name="tableCode" class="easyui-textbox" data-options="prompt: 'tableCode'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>colCode:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="colCode" name="colCode" class="easyui-textbox" data-options="prompt: 'colCode'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>comCode:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="comCode" name="comCode" class="easyui-textbox" data-options="prompt: 'comCode'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>rowNo:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="rowNo" name="rowNo" class="easyui-textbox" data-options="prompt: 'rowNo'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>colType:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="colType" name="colType" class="easyui-textbox" data-options="prompt: 'colType'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>numValue:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="numValue" name="numValue" class="easyui-textbox" data-options="prompt: 'numValue'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>wanValue:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="wanValue" name="wanValue" class="easyui-textbox" data-options="prompt: 'wanValue'"  style="width:60%;">
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
		    				<label>reportType:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportType" name="reportType" class="easyui-textbox" data-options="prompt: 'reportType'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>yearMonth:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="yearMonth" name="yearMonth" class="easyui-textbox" data-options="prompt: 'yearMonth'"  style="width:60%;">
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
		    				<label>reportRate:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportRate" name="reportRate" class="easyui-textbox" data-options="prompt: 'reportRate'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>source:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="source" name="source" class="easyui-textbox" data-options="prompt: 'source'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>operator2:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="operator2" name="operator2" class="easyui-textbox" data-options="prompt: 'operator2'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>operDate2:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="operDate2" name="operDate2" class="easyui-textbox" data-options="prompt: 'operDate2'"  style="width:60%;">
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
			url=${pageContext.request.contextPath}/cfReportRowAddDataRisk/listpage.do
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
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCfReportRowAddDataRisk()">新建</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editCfReportRowAddDataRisk()">编辑</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="destroyCfReportRowAddDataRisk()">删除</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="tableCode" width="25" sortable="true">tableCode</th>
					<th field="colCode" width="25" sortable="true">colCode</th>
					<th field="comCode" width="25" sortable="true">comCode</th>
					<th field="rowNo" width="25" sortable="true">rowNo</th>
					<th field="colType" width="25" sortable="true">colType</th>
					<th field="numValue" width="25" sortable="true">numValue</th>
					<th field="wanValue" width="25" sortable="true">wanValue</th>
					<th field="textValue" width="25" sortable="true">textValue</th>
					<th field="desText" width="25" sortable="true">desText</th>
					<th field="reportType" width="25" sortable="true">reportType</th>
					<th field="yearMonth" width="25" sortable="true">yearMonth</th>
					<th field="quarter" width="25" sortable="true">quarter</th>
					<th field="reportRate" width="25" sortable="true">reportRate</th>
					<th field="source" width="25" sortable="true">source</th>
					<th field="operator2" width="25" sortable="true">operator2</th>
					<th field="operDate2" width="25" sortable="true">operDate2</th>
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
	    				<label>tableCode:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="tableCode" type="text" name="tableCode" class="easyui-textbox" data-options="prompt: 'tableCode'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>colCode:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="colCode" type="text" name="colCode" class="easyui-textbox" data-options="prompt: 'colCode'"  style="width:60%;">
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
	    				<label>rowNo:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="rowNo" type="text" name="rowNo" class="easyui-textbox" data-options="prompt: 'rowNo'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>colType:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="colType" type="text" name="colType" class="easyui-textbox" data-options="prompt: 'colType'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>numValue:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="numValue" type="text" name="numValue" class="easyui-textbox" data-options="prompt: 'numValue'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>wanValue:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="wanValue" type="text" name="wanValue" class="easyui-textbox" data-options="prompt: 'wanValue'"  style="width:60%;">
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
	    				<label>reportType:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportType" type="text" name="reportType" class="easyui-textbox" data-options="prompt: 'reportType'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>yearMonth:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="yearMonth" type="text" name="yearMonth" class="easyui-textbox" data-options="prompt: 'yearMonth'"  style="width:60%;">
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
	    				<label>reportRate:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportRate" type="text" name="reportRate" class="easyui-textbox" data-options="prompt: 'reportRate'"  style="width:60%;">
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
	    				<label>operator2:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="operator2" type="text" name="operator2" class="easyui-textbox" data-options="prompt: 'operator2'"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:10%;" >
	    				<label>operDate2:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="operDate2" type="text" name="operDate2" class="easyui-textbox" data-options="prompt: 'operDate2'"  style="width:60%;">
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
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveCfReportRowAddDataRisk()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	    </div>
    </div>
</body>
<script  type="text/javascript">
// 新建
    function newCfReportRowAddDataRisk(){
    	$('#dlg').dialog('open').dialog('setTitle','新增');
    	$('#fm').form('clear');
    	url='${pageContext.request.contextPath}/cfReportRowAddDataRisk/add.do';
	}
//保存
function saveCfReportRowAddDataRisk(){
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
function editCfReportRowAddDataRisk(){
    var rows = $('#dg').datagrid('getSelections');
    if(!rows){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','编辑');
	$('#fm').form('load',rows[0]);
	url = '${pageContext.request.contextPath}/cfReportRowAddDataRisk/update.do?id='+rows[0].id;
}

//删除
function destroyCfReportRowAddDataRisk(){
	var rows = $('#dg').datagrid('getSelections');
	var idArr = new Array();
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].id;
	}
	var data =[{name:'ids',value:idArr.join(',') }];
	if(rows.length==0){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
		$.messager.confirm('删除','确认删除吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/cfReportRowAddDataRisk/delete.do',data,function(result){
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