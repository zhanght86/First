<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>数据接口处理</title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@include file="/commons/statics.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/color.css' />">

</head>
<body >
<!-- 搜索模块 -->
	<div class="easyui-panel"  style="padding: 10px;width: 100%" data-options="footer:'#ft'">		
		<form id="serachFrom" method="post">
		<input type="hidden" name="department" value="02">
			<table style="width: 100%;">
		    		<tr height="30">
			    		<td style="width:10%;" >
		    				<label>报送年度:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="year" name="year" class="easyui-textbox" data-options="prompt: '报送年度'"  style="width:60%;">
		    			</td>
		    			<td style="width:10%;" >
		    				<label>报送季度:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="quarter" name="quarter" class="easyui-combobox" editable="false" style="width:60%;"
		    				 data-options="
					        prompt: '报送季度',
					        missingMessage:'请添加季度',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group'
					    	">
		    			</td>
		    			<td style="width:20%;" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">搜索</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
	    				</td>
				    </tr>
		   </table>
		</form>	
	</div>
	<!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog"
		style="width: 300px; height:100px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,onOpen:onOpen,onClose:onClose">
		<div id="msg"></div>
    </div>
 <!-- 	操作，展示部分 -->
	 <div class="easyui-panel" style="border: 0;width: 100%">
		<table id="dg" class="easyui-datagrid"
		data-options="
			url:'${pageContext.request.contextPath}/calCfReportInfo/listpage1.do',
			iconCls:'icon-edit',
			toolbar:'#toolbar' ,
			rownumbers:'true',
			fitColumns:'true',
			singleSelect:'true', 
			pagination:'true', 
			striped:'true',
			nowrap:'true',
			onClickRow:onClickRow,
			autoRowHeight:'false',
			style:'width:100%;height:auto;',
			loadMsg:'加载数据中...'
			"
			>
			<div id="toolbar">
   				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="deal()" plain="true"  data-options="iconCls:'icon-print'" style="width:80px;">数据处理</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="25" >报送号</th>
					<th field="year" width="25" >报送年度</th>
					<th field="quarter" width="25" hidden>报送季度</th>
					<th field="quarterName" width="25" >报送季度</th>
					
					<th field="impdate" width="25" 
				 	formatter="resultCategory" 
						editor="{type:'datebox'}"
					>日期(请先选择日期)</th> 
					<!-- 
					<th field="reportType" width="25" hidden>报送类型</th>
					<th field="reportTypeName" width="25" >报送类型</th>
					<th field="reportCategory" width="30" 
						formatter="resultCategory"
						editor="{type:'combobox',options:{
								valueField:'value',
								textField:'text',
								method:'get',
								url:'${pageContext.request.contextPath}/codeSelect/hasParam.do?type=reportname&params=2',
							}}">报送类别（数据下载时选择）</th>
					<th field="reportState" width="25" hidden>报送状态</th>
					-->
				</tr>
			</thead>
		</table>
	</div>
</body>
<script  type="text/javascript">
function resultCategory(value,row){
	return row.value;
}

var editIndex = undefined;
function endEditing(){
	if (editIndex == undefined){return true}
	if ($('#dg').datagrid('validateRow', editIndex)){
		var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:'impdate'});
		var name = $(ed.target).datebox('getValue');
		
		$('#dg').datagrid('getRows')[editIndex]['value'] = name;
		$('#dg').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}

function onClickRow(index){
	if (editIndex != index){
		if (endEditing()){
			$('#dg').datagrid('selectRow', index)
					.datagrid('beginEdit', index);
			editIndex = index;
		} else {
			$('#dg').datagrid('selectRow', editIndex);
		}
	}
}

var loading
function onOpen(){
	 loading=setInterval(showalert, 500); 
}
var i=2;
function showalert() { 
		var text="";
			if (i == 1) {
				text = '正在处理，请稍后.';
			} else if (i == 2) {
				text = '正在处理，请稍后..';
			} else if (i == 3) {
				text = '正在处理，请稍后...';
				i = 0;
			}
		i++;
		$('#msg').text(text);
} 
function onClose(){
	clearInterval(loading); 
}

//重置
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
	endEditing();
}

function deal(){
	var rows = $('#dg').datagrid('getSelections');
	if(rows.length==0||rows.length>1){
		$.messager.alert('消息提示','请选择一条报送号进行数据处理!','error');
		return ;
	}
	endEditing();
	var year = rows[0].year;
	var quarter = rows[0].quarter;
	var reportId = rows[0].reportId;
	var impdate = rows[0].impdate;
	if(impdate==0||impdate==null){
		$.messager.alert('消息提示','请选择日期!','error');
		return ;
	}
	var data = 'year=' + year + '&quarter=' + quarter +'&reportId=' + reportId +'&impdate=' + impdate;
	//alert(data);
	$('#serachFrom').form('submit',{
		url: '${pageContext.request.contextPath}/InterDeal/datadeal.do?' +data,
 		onSubmit: function(){
 			return checkinput();
 		},
		 success: function(result){
			$('#dlg').dialog('close')
			var result = eval('('+result+')');
			if (!result.success){
				jsutil.msg.err('处理失败:'+result.errorMsg)
			} else {
				jsutil.msg.alert('处理成功')
			}
		} 
	});
}


function checkinput(){
	if($('#serachFrom').form('validate')){
		$('#msg').text('正在处理，请稍后.');
		$('#dlg').dialog('open').dialog('setTitle','数据处理');
        return true;
	}else{
		return false;
	}
}








</script>
</html>