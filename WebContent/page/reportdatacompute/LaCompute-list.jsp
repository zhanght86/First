<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/validateyear.js"></script>
<%
	String path=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); 
%>
<title>计算损失吸收效应</title>
</head>
<body>
<%@include file="/commons/statics.jsp"%>
	<!-- 搜索模块 -->
	<div class="easyui-panel"  style="padding: 10px;width: 100%" data-options="footer:'#ft'">
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
		    			<td style="width:20%;" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">搜索</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
	    				</td>
				    </tr>
				    </table>
		   <div class="easyui-panel" id="searchmore" style="height:auto; width:inherit;border:0;margin:0" data-options="collapsed:true">
				<table style="width: 100%;" >
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
		    			<td style="width:20%;">
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
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=cfreportstate',
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
		    			<td style="width:20%;">
		    			</td>
				    </tr>		    		
	    	</table>
	    	</div>
		</form>		
	</div>
	<div id="ft" style="text-align:center;width:auto;border-top: 0;background: #fff;">
        	<a href="#" style="height:20px;border-top: 0;border: 1px solid #95B8E7;border-bottom: 0;border-bottom-right-radius: 0;
   			 border-bottom-left-radius: 0;"  class="easyui-linkbutton" id="lkbtn" iconCls="icon-arrows-down" plain="true" onclick="moresearch()">更多条件</a>
    </div>
<!-- 	操作，展示部分 -->
	 <div class="easyui-panel" style="border: 0;width: 100%">
		<table id="dg" class="easyui-datagrid"
		data-options="
			url:'${pageContext.request.contextPath}/calCfReportInfo/listpage.do',
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
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="laCompute()">La计算</a> 
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="25" >报送号</th>
					<th field="year" width="25" >报送年度</th>
					<th field="quarter" width="25" hidden>报送季度</th>
					<th field="quarterName" width="25" >报送季度</th>
					<th field="reportType" width="25" hidden>报送类型</th>
					<th field="reportTypeName" width="25" >报送类型</th>
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
</body>
<script  type="text/javascript">
function isYON(value){
	if(value=='0'){
		return '否';
	}else{
		return '是';
		} 
}
function resultCategory(value,row){
	return row.value;
}
var editIndex = undefined;
function endEditing(){
	if (editIndex == undefined){return true}
	if ($('#dg').datagrid('validateRow', editIndex)){
		var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:'reportCategory'});
		var name = $(ed.target).combobox('getText');
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
// 计算la
   function laCompute(){
	var rows = $('#dg').datagrid('getSelections');
	if(rows.length==0||rows.length>1){$.messager.alert('消息提示','请选择一条报送号损失吸收效应计算!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','损失吸收效应计算');
	$('#msg').text('正在进行初步计算，请稍后.');
	var data =[{name:'reportId',value:rows[0].reportId }];
	$.post('${pageContext.request.contextPath}/laCompute/laCompute.do',data,function(result){
		if(result.success){
			$('#dlg').dialog('close');		// close the dialog
			$('#dg').datagrid('reload');	// reload the user data
			//endEditing();
			jsutil.msg.alert("La计算完成");
		}else {
			$('#dlg').dialog('close');		// close the dialog
			$('#dg').datagrid('reload');	// reload the user data
			//endEditing();
			jsutil.msg.alert(result.errorMsg);
		}
	},'json');
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
			text='正在进行操作，请稍后.';
		}else if(i==2){
			text='正在进行操作，请稍后..';
		}else if(i==3){
			text='正在进行操作，请稍后...';
			i=0;
		}
		i++;
		$('#msg').text(text);
} 
function onClose(){
	clearInterval(loading); 
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
	endEditing();
}
//高级搜索框

function moresearch(){
	$('#lkbtn').blur();
		var flag=$('#searchmore').panel('options').collapsed;
		if(flag){
			//展开
			$('.icon-arrows-down').addClass('icon-arrows-up');
			$('.icon-arrows-down').removeClass('icon-arrows-down');
			$('#searchmore').panel('expand',true); 
			flag=false;
		}else{
			//关闭
			$('.icon-arrows-up').addClass('icon-arrows-down');
			$('.icon-arrows-up').removeClass('icon-arrows-up');
			$('#searchmore').panel('collapse',true);
			flag=true;
			reset();
		}
}
</script>
</html>