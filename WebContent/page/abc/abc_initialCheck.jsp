<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/validateyear.js"></script>
<title>数据校验条件</title>

<script  type="text/javascript">
//重置
function reset(){
	$('#serachFrom').form("clear");
	$('#dataDiv').panel('close');
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
function dataCheck(){
	var rows = $('#dg').datagrid('getSelections');
	if(rows.length==0||rows.length>1){$.messager.alert('消息提示','请选择一条报送号进行数据校验!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','数据校验');
	$('#msg').text('正在进行数据校验，请稍后.');

//	var data =[{name:'reportId',value:rows[0].reportId }];
    var data =[{name:'reportId',value:rows[0].reportId },{name:'quarter',value:rows[0].quarter }
    ,{name:'year',value:rows[0].year }];
	$.post('${pageContext.request.contextPath}/dataCheck/initialCheck.do',data,function(result){
		if (!result.success){
			$('#dlg').dialog('close');		// close the dialog
			$('#dg').datagrid('reload');	// reload the user data
			$.messager.alert("提示","校验无错误数据","info");
			$('#dataDiv').panel('close');
		} else {
			$('#dlg').dialog('close');		// close the dialog
			//$('#dg').datagrid('reload');	// reload the user data
			$.messager.alert("警告","数据校验错误，请查看数据校验错误结果","warning",function(){
				$("#dg1").datagrid({data:result.data});
				$('#dataDiv').panel('open');
			});
		}
	},'json');
} 
function updateState(){
	var rows = $('#dg').datagrid('getSelections');
	if(rows.length==0||rows.length>1){$.messager.alert('消息提示','请选择一条报送号进行操作!','error');return ;}
	if(rows[0].reportState!="5"){
		jsutil.msg.alert('只有校验完毕状态的报送单才可以进行重新校验！');
		return false;
	}
	var data =[{name:'reportId',value:rows[0].reportId }];
	$.post('${pageContext.request.contextPath}/dataCheck/updateState.do',data,function(result){
		if (result.success){
			$('#dg').datagrid('reload');	// reload the user data
			$.messager.alert("提示","状态改变成功","info");
		} else {
			$.messager.alert("警告","状态改变错误","warning");
		}
	},'json');
} 
function checkinput(){
	var year = $("#year").val().trim();
	if(!/\s*\d{4}\s*/.test(year)) {
		jsutil.msg.alert('年度必须是4个数字！');
		$('#dataDiv').panel('close');
		$('#exportbtn').linkbutton('disable');  
		return false;
	}
	if(!$('#dataCheckForm').form('validate')){
		$('#dataDiv').panel('close');
		$('#exportbtn').linkbutton('disable');  
		return false;
	}
	$('#msg').text('正在检验，请稍后.');
	$('#dlg').dialog('open').dialog('setTitle','正在检验');
	return true;
}

var loading
function onOpen(){
	 loading=setInterval(showalert, 500); 
}

var i=2;
function showalert() 
{ 
		var text="";
		if(i==1){
			text='正在检验，请稍后.';
		}else if(i==2){
			text='正在检验，请稍后..';
		}else if(i==3){
			text='正在检验，请稍后...';
			i=0;
		}
		i++;
		$('#msg').text(text);
} 

function onClose(){
	clearInterval(loading); 
}

function exportXls(gridId, xlsName) {
	var param=jsutil.core.exportXls(gridId,xlsName);
	jsutil.core.download("${pageContext.request.contextPath}/downloadExcel",param);
}
function isYON(value){
	if(value=='0'){
		return '否';
	}else{
		return '是';
		} 
}
</script>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
	<!-- 搜索模块 -->
	<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
		<form id="serachFrom" method="post">
			<table style="width: 100%;">
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>报送号:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportId" name="reportId" class="easyui-textbox" data-options="prompt: '报送号'"  style="width:60%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>报送年度:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="year" name="year" class="easyui-textbox" data-options="prompt: '报送年度'"  style="width:60%;">
		    			</td>
				    </tr>
		    		<tr height="30">
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
			    	<td style="width:10%;" >
		    				<label>报送类型:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportType" name="reportType" class="easyui-combobox" editable="false" style="width:60%;"
		    				data-options="
					        prompt: '报表类别',
					        missingMessage:'请添加报表类别',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=reporttype',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group'">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>报送状态:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportState" name="reportState" class="easyui-combobox"  style="width:60%;"
		    				data-options="
					        prompt: '报送状态',
						    url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=cfreportstate&params=4,4.5',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group'">
		    			</td>
			    	<td style="width:10%;" >
		    				<label>是否完成手工导入:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<select id="handleFlag" name="handleFlag" class="easyui-combobox" data-options="prompt: '是否完成手工导入'"  editable="false" style="width:60%;">
		    					<option value=""></option>
		    					<option value="0">否</option>
		    					<option value="1">是</option>
		    				</select>
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    			</td>
		    			<td style="width:30%;">
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
			url=${pageContext.request.contextPath}/calCfReportInfo/listpagehasparam.do?type=complexcompute
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="dataCheck()">数据校验</a> 
<!-- 				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updateState()">重新进行数据校验</a> 
 -->			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="25" >报送号</th>
					<th field="year" width="25" >报送年度</th>
					<th field="quarter" width="25" hidden>报送季度</th>
					<th field="quarterName" width="25" >报送季度</th>
					<th field="reportType" width="25" hidden>报送类型</th>
					<th field="reportTypeName" width="25" >报送类型</th>
					<th field="reportState" width="25" hidden>报送状态</th>
					<th field="reportStateName" width="25" >报送状态</th>
					<th field="handleFlag" width="25" formatter="isYON">是否完成手工导入</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog"
		style="width: 300px; height:100px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,onOpen:onOpen,onClose:onClose">
		<div id="msg"></div>
    </div>
    <br>
    <br>
<!-- 展示部分 -->
<div class="easyui-panel" closed="true" style="border: 0;width: 100%; height:366px;" id="dataDiv">
	    <table id="dg1"  class="easyui-datagrid" style="width:100%;"
			rownumbers="true"
			fitColumns="true"
			singleSelect="true"
			pagination="false" 
			striped="true"
			title="校验结果信息"
			nowrap="true"
			autoRowHeight="false"
			style="width:90%;height:320px;"
			loadMsg="加载数据中..."
			>
			<div id="toolbar">
				<a href="javascript:void(0)" id="exportbtn" class="easyui-linkbutton" onclick="exportXls('dg1','数据校验')" data-options="iconCls:'icon-print'" style="width:80px;">导出</a> 
			</div>
      	<thead>
    		<tr>  
     			<th field="year"  align="center">年度</th>
    			<th field="quarter"  align="center">季度</th>
    			<th field="reporttype" align="center">报告类型</th>
    			<th field="checkedCode" align="center">错误级别</th>
    			<th field="temp" width="8" align="center">校验公式</th>
    			<th field="errorInfo" width="25" align="center">校验信息</th>
    			 <th field="checkdate"  align="center">校验时间</th>

    	</tr>
    	</thead>
    </table>
 </div>
</body>
</html>