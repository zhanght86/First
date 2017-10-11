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
<title>CalCfReportInfo管理</title>
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
		    				<label>报送号:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportId" name="reportId" class="easyui-textbox" data-options="prompt: '报送号'"  style="width:100%;">
		    			</td>
			    		<td style="width:10%;text-align: right;" >
		    				<label>报送年度:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<!-- <input id="year" name="year" class="easyui-textbox" data-options="prompt: '报送年度'"  style="width:100%;"> -->
		    				<input style="width: 100%;" id="year" name="year" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
		    			</td>
		    			<td style="width:8%;text-align: right;" >
		    				<label>报送季度:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="quarter" name="quarter" class="easyui-combobox" editable="false" style="width:100%;"
		    				 data-options="  prompt: '报送季度', missingMessage:'请添加季度',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
						    method: 'get', valueField:'value', textField:'text', groupField:'group'">
		    			</td>
		    			<td style="width:8%;text-align: right;" >
		    				<label>报送类型:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<select class="easyui-combobox" id="reportRate" name="reportRate" style="width:100%;" readonly>
							<option value="2">季度报告</option>
					    	</select>
		    			</td>
				    </tr>
				    <tr>
				    <!-- 
				    <td style="width:8%;text-align: right;" >
		    				<label>报送状态:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportState" name="reportState" class="easyui-combobox"  style="width:100%;"
		    				data-options="  prompt: '报送状态',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=cfreportstate',
						    method: 'get',  valueField:'value', textField:'text', groupField:'group'">
		    			</td>
		    			<td style="width:10%;text-align: right;" >
		    				<label>是否完成手工导入:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<select id="handleFlag" name="handleFlag" class="easyui-combobox" data-options="prompt: '是否完成手工导入',editable:false"  editable="false" style="width:100%;">
		    					<option value=""></option>
		    					<option value="0">否</option>
		    					<option value="1">是</option>
		    				</select>
		    			</td>
			    		
		    			 -->
			    		
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
	<!-- <div id="ft" style="text-align:center;width:auto;border-top: 0;background: #fff;">
        	<a href="#" style="height:20px;border-top: 0;border: 1px solid #95B8E7;border-bottom: 0;border-bottom-right-radius: 0;
   			 border-bottom-left-radius: 0;"  class="easyui-linkbutton" id="lkbtn" iconCls="icon-arrows-down" plain="true" onclick="moresearch()">更多条件</a>
    </div> -->
<!-- 	操作，展示部分 -->
	<div  class="easyui-panel" style="height: auto; width:100%; overflow: hidden;">
		<table id="dg" class="easyui-datagrid"
		data-options="
			iconCls:'icon-edit',
			toolbar:'#toolbar' ,
			rownumbers:'true',
			fitColumns:'true',
			singleSelect:'true', 
			pagination:'true', 
			striped:'true',
			nowrap:'false',
			onClickRow:onClickRow,
			autoRowHeight:'false',
			style:'width:100%;height:auto;',
			loadMsg:'加载数据中...'
			"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			>
			<div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCalPreCompute()">汇总计算</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-print" plain="true" onclick="modeldownloaddate()">数据下载</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="destroyCalPreCompute()">撤销汇总计算</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="25%" align="center" halign="center" >报送号</th>
					<th field="year" width="18%"align="center" halign="center" >报送年度</th>
					<th field="quarter" width="15%" hidden>报送季度</th>
					<th field="quarterName" width="18%" align="center" halign="center">报送季度</th>
					<th field="reportType" width="14%" hidden>报送类型</th>
					<th field="reportTypeName" width="18%" align="center" halign="center">报送类型</th>
					<th field="reportCategory" width="18%" align="center" halign="center"
						formatter="resultCategory"
						editor="{type:'combobox',								
								options:{
								editable:false,
								valueField:'value',
								textField:'text',
								method:'get',
								url:'${pageContext.request.contextPath}/codeSelect/hasParam.do?type=reportname&params=1',
							}}">报送类别</th>
					<!-- 
					<th field="reportState" width="14%" hidden>报送状态</th>
					<th field="reportStateName" width="14%" align="center" halign="center" >报送状态</th>
					<th field="handleFlag" width="14%" align="center" halign="center" formatter="isYON">是否完成手工导入</th> -->
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
$(function(){
	$('#year').combobox({
	    disabled:false,
	    prompt:'年度',
	    valueField:'value',
	    textField:'text',
	    url:'${contextPath}/json/year.json'
	});
	serach();
});
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
		if(ed==null){
			editIndex == undefined;
			return true;
		}
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

//数据下载
function modeldownloaddate(){	
	var rows = $('#dg').datagrid('getSelections');
	if(rows[0].reportState!="4"){
		$.messager.alert('消息提示','下载必须要在汇总计算完成后!','error');
		return false;
	}
	endEditing();
	if(rows[0].reportCategory==""||rows[0].reportCategory==null){
		$.messager.alert('消息提示','请选择报送类别!','error');
		return false;
	}
	$('#dlg').dialog('open').dialog('setTitle','数据下载');
	$('#msg').text('正在下载数据，请稍后.');
	var data =[{name:'yearmonth',value:rows[0].year},{name:'quarter',value:rows[0].quarter},{name:'reporttype',value:rows[0].reportType},{name:'reportname',value:rows[0].reportCategory},{name:'reportId',value:rows[0].reportId}];
	$.post('${pageContext.request.contextPath}/commonImportDate/modeldownloaddate.do',data,function(result){
		$('#dlg').dialog('close');
		if (result.success){
			location.href = '${pageContext.request.contextPath}/downLoadReport/downLoad.do?filepath='+result.data;			
		} else {
			jsutil.msg.err('下载失败'+result.errorMsg);
		}
	},'json'); 
}
// 新建
   function newCalPreCompute(){
	var rows = $('#dg').datagrid('getSelections');
	if(rows.length==0||rows.length>1){$.messager.alert('消息提示','请选择一条报送号进行汇总计算!','error');return ;}
	
	endEditing();
	if(rows[0].reportCategory==""||rows[0].reportCategory==null){
		$.messager.alert('消息提示','请选择报送类别!','error');
		return false;
	}
	$('#dlg').dialog('open').dialog('setTitle','汇总计算');
	$('#msg').text('正在进行汇总计算，请稍后.');
	var data =[{name:'reportId',value:rows[0].reportId },{name:'reporttype',value:rows[0].reportCategory}];
	$.post('${pageContext.request.contextPath}/calComplexCompute/complexcompute.do',data,function(result){
		if (result.success){
			$('#dlg').dialog('close');		// close the dialog
			$('#dg').datagrid('reload');	// reload the user data
			endEditing();
			jsutil.msg.alert("汇总计算完成");
		} else {
			$('#dlg').dialog('close');		// close the dialog
			$('#dg').datagrid('reload');	// reload the user data
			endEditing();
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

//删除
function destroyCalPreCompute(){
	var rows = $('#dg').datagrid('getSelections');
	if(rows.length==0||rows.length>1){$.messager.alert('消息提示','请选择一条报送号进行汇总计算!','error');return ;}
	
	endEditing();
	if(rows[0].reportCategory==""||rows[0].reportCategory==null){
		$.messager.alert('消息提示','请选择报送类别!','error');
		return false;
	}
	
	var data =[{name:'reportId',value:rows[0].reportId },{name:'reporttype',value:rows[0].reportCategory}];
	$.messager.confirm('撤销操作','确认撤销吗?',function(r){
		if (r){
			$.post('${pageContext.request.contextPath}/calComplexCompute/backprecompute.do',data,function(result){
				if (result.success){
					$('#dg').datagrid('reload');	// reload the user data
					jsutil.msg.alert("撤销成功");
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
	//endEditing();
	$("#dg").datagrid({
		url:'${pageContext.request.contextPath}/calCfReportInfo/listpage.do?reportCateGory=0&reportType=2',
		queryParams: params,
		onBeforeLoad: function(){
			if(!$('#serachFrom').form('validate')) return false;
		},
		onLoadSuccess: function(data){
			if(data.total>0) {
				editIndex = undefined;
				endEditing();
 				return  false;
 			}
 			$.messager.alert("提示","未查询到相关数据","info");
		}
	});
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