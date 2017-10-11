<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/validateyear.js"></script>
<title>上传文件清单查询</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
	<!-- 操作部分 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form id="serachFrom" method="post" style="margin-bottom: 0">
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">						
	    		<tr>
	    			<td style="width:8%;text-align: right;" >
	    				<label>报告类型:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input class="easyui-combobox" id="reporttype" name="reporttype" style="width:100%"
						data-options=" editable:false,url: '<%=path%>/codeSelect.do?type=reporttype',
						prompt:'报告类型',method: 'get',valueField:'value',textField:'text',panelHeight:'auto',groupField:'group'">
	    			</td>
	    			 <td style="width:8%;text-align: right;" >
	    				<label>报告名称:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input  class="easyui-combobox" id="reportname" name="reportname" style="width: 100%;" 
	    				data-options=" prompt: '报告名称',editable:false,url:'<%=path%>/codeSelect.do?type=reportname',
	    				method: 'get',valueField:'value',textField:'text',panelHeight:'auto',groupField:'group'" >
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>年度:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<!-- <input  id="year" name="year" validType="yearValidation['^[2][0-1][0-9][0-9]$','请输入合法的年份，例如：2016']" class="easyui-textbox" data-options="prompt: '年度请输入4个数字'"  style="width:100%;"> -->
	    				<input style="width: 100%;" id="year" name="year" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
	    			</td>
	    			
	    			<td style="width:8%;text-align: right;"  >
	    				<label>季度:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input  id="quarter"   name="quarter"  class="easyui-combobox"  style="width:100%;" data-options="prompt: '季度', editable:false,url:'<%=path%>/codeSelect.do?type=quarter',method: 'get',valueField:'value',textField:'text',panelHeight:'auto',groupField:'group'" >
	    			</td>	
	    		</tr>  
	 			<tr>
	    			<td colspan="8" style="text-align: right;padding-right: 20px" >
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">查询</a>
	    				<!-- <a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportXls('dg','上传文件清单')" data-options="iconCls:'icon-print'"  style="width:8%;" >导出</a>  -->
	    				<a href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-print'"  style="width:8%;" onclick="exportByConditionAll()">导出</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:8%;">重置</a>
					</td>
	    		</tr>
	    	</table>
		
	</form>	
</div>  
<!-- 展示部分 -->
	 <div id="showRecord" class="easyui-panel" style="border: 0;width: 100%">
	    <table id="dg" class="easyui-datagrid" style="width:100%;"
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="false"
			sortName="roleCode"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar">
				<a href="#" id="download" class="easyui-linkbutton" iconCls="icon-print" plain="true"  onclick="downloadFile()">下载文件</a> 
			</div>
      	<thead>
    		<tr>  
    		    <th field="ck" checkbox=true></th>
    		    <th field="yearMonth" width="5%" align="center" halign="center">年度</th>
    			<th field="quarter"  width="5%" align="center" halign="center">季度</th>
    			<th field="reportType" width="6%" align="center" halign="center">报告类型</th>
    			<th field="reportName" width="8%" align="center" halign="center">报告名称</th>

     			<th field="fileName" width="20%" align="left" halign="center">上传文件名称</th>
    			<th field="filepath" width="32%" align="center" halign="center">上传文件路径</th>
    			<th field="operator" width="6%" align="center" halign="center">操作人</th>
    			<th field="operatdate" width="8%" align="center" halign="center">操作日期</th>
    			<th field="remark" width="6%" align="center" halign="center">备注</th> 
    			<th field="uploadNo" width="6%" align="center" halign="center" hidden="true">文件编号</th> 

    	</tr>
    	</thead>
    </table>
  </div>
    	
	<div id="dlg" class="easyui-dialog" closed="true" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlg-buttons">
		<form id="downloadForm" method="post">
			<table >
			<tr>
	    			<td><input  type="text" id="length" name="length" type="hidden" ></td>
	    			<td><input  type="text" id="dlPath" name="dlPath" type="hidden"></td>
	    			<td><input  type="text" id="uploadNo" name="uploadNo" type="hidden"></td>
	    		</tr>
	    	</table>
	    	
		</form>
		
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

//准备参数
function prepareParam(xlsName){
	var param = {name: xlsName, queryConditions: {}, cols: []};
	var grid = $("#dg");
	//返回加载完毕后的数据
	var datas = grid.datagrid("getRows");
	if (datas.length > 0) {
		//返回列字段
		var cols = grid.datagrid("getColumnFields");
 		 if (cols.length > 0 && cols[0] == "ck") {
			cols.shift();
		}
		var col;
		for (var i = 0; i < cols.length; i++) {
			//返回指定列属性
			col = grid.datagrid("getColumnOption", cols[i]);
			param.cols[i] = {field: col.field, title: col.title, width: col.width};
		
		}
		/* param.reporttype=$('#reporttype').combobox('getValue');
		param.reportname=$('#reportname').combobox('getValue');
		param.year=$('#year').combobox('getValue');
		param.quarter=$('#quarter').combobox('getValue'); */
		 $('#serachFrom').find('input').each(function(){
    		var obj = $(this);
    		var name =obj.attr('name');
    		 if(name){
        			param.queryConditions[name]=obj.val();
    		}
    	}); 
		//console.log(param);
		return param;
	} else {
		$.messager.alert("提示","请先查询或者没有要导出的数据","warning");
		return "false";
	}
};
function exportByConditionAll(){
	//准备参数
	var xlsName = '上传文件清单';
	var result = prepareParam(xlsName); 
	if(result=="false") {
		return false;
	}
	//将查询数据导出
	jsutil.core.download("${contextPath}/uploadFiles/downloadFilebill",result);
}

//查找上传文件清单信息
function serach(){
	$('#showRecord').show();
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
   	    url:'${pageContext.request.contextPath}/uploadFiles/list.do',
   	     	queryParams: params,
   			onBeforeLoad: function(){
   				if(!$('#serachFrom').form('validate')) return false;
   			},
   			onLoadSuccess: function(data){
   				if(data.total>0) {
   	 				return false;
   	 			}
   	 			$.messager.alert("提示","未查询到相关数据","info");
   			}
   		}); 
 }

//重置
function reset() {
	$('#serachFrom').form("clear");
	$('#showRecord').hide();
}
function exportXls(gridId, xlsName) {
	var param=jsutil.core.exportXls(gridId,xlsName);
	jsutil.core.download("${pageContext.request.contextPath}/downloadExcel",param);
}

function downloadFile(){
	var rows = $('#dg').datagrid('getSelections');
	
  	if(rows.length==0){
		jsutil.msg.err('请选择要下载的数据信息!');
		return false;
	}
	
	var idArr = new Array();
	var data = "";
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].filepath + "/" + rows[i].fileName;
		data = data + idArr[i] + ",";
	}
	//设置text里的值，用的是jquery
	$("#length").val(rows.length);
	$("#uploadNo").val(rows[0].uploadNo);
	$("#dlPath").val(data);
	
	$('#downloadForm').form('submit',{ 
		url: '${pageContext.request.contextPath}/uploadFiles/download.do',
		
	success: function(result){
		var result = eval('('+result+')');
		if (result.errorMsg){
			jsutil.msg.err(result.errorMsg);
		} else {
			jsutil.msg.alert('下载成功!');
		}
	}
	});  
}

</script>
</html>