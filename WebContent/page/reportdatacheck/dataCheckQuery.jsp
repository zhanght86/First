<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据校验</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/validateyear.js"></script>
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
function prepareParam1(xlsName){
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
		if (cols.length > 0 && cols[0] == "id") {
			cols.shift();
		}
		var col;
		for (var i = 0; i < cols.length; i++) {
			//返回指定列属性
			col = grid.datagrid("getColumnOption", cols[i]);
			param.cols[i] = {field: col.field, title: col.title, width: col.width};
		
		}
		param.reporttype=$('#reporttype').combobox('getValue');
		param.year=$('#year').combobox('getValue');
		param.quarter=$('#quarter').combobox('getValue');
		param.checkedCode=$('#checkedCode').combobox('getValue');
		/* $('#serachFrom').find('input').each(function(){
    		var obj = $(this);
    		var id =obj.attr('id');
    		 if(id){
        			param.queryConditions[id]=obj.combobox('getValue');
    		}
    	}); */
		//console.log(param);
		return param;
	} else {
		$.messager.alert("提示","请先查询或者没有要导出的数据","warning");
		return "false";
	}
};
function exportByConditionAll(){
	//准备参数
	var xlsName = '数据校验结果';
	var result = prepareParam1(xlsName); 
	if(result=="false") {
		return false;
	}
	//将查询数据导出
	jsutil.core.download("${contextPath}/downloadCheck/downCheckResult",result);
}
//查找校验错误信息
function serach(){
	var params = {};
	$('#serachFrom').find('input').each(function(){
       var obj = $(this);
       var name = obj.attr('name');
       if(name){
           params[name] = obj.val();
       }
   }); 	
	  $("#dg").datagrid({
        url:'${pageContext.request.contextPath}/dataCheck/list.do',
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

function exportXls(gridId, xlsName) {
	var param=jsutil.core.exportXls(gridId,xlsName);
	jsutil.core.download("${pageContext.request.contextPath}/downloadExcel",param);
}

</script>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
	<!-- 操作部分 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form id="serachFrom" method="post" style="margin-bottom: 0">
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">				
	    		<tr>
	    			<td style="width:8%;text-align: right;">
	    				<label>报告类型:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input class="easyui-combobox" id="reporttype" name="reporttype" style="width:100%;" data-options="url: '<%=path%>/codeSelect.do?type=reporttype',prompt:'报告类型',method: 'get',valueField:'value',editable:false,textField:'text'">
	    			</td>
	    			
	    		   <td style="width:8%;text-align: right;" >
	    				<label>年度:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<!-- <input  id="year" name="year" class="easyui-textbox" validType="yearValidation['^[2][0-1][0-9][0-9]$','请输入合法的年份，例如：2016']" data-options="prompt: '年度'"  style="width:100%;"> -->
	    				<input style="width: 100%;" id="year" name="year" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
	    			</td>
	    			<td style="width:8%;text-align: right;">
	    				<label>季度:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input  id="quarter"   name="quarter"  class="easyui-combobox"  style="width:100%;"  
	    				data-options="prompt: '季度', url:'<%=path%>/codeSelect.do?type=quarter',
	    				method: 'get',valueField:'value',editable:false,textField:'text',groupField:'group'" >
	    			</td>
	    			 <td style="width:8%;text-align: right;" >
	    				<label>错误级别:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input  id="checkedCode"   name="checkedCode" class="easyui-combobox"  style="width: 100%;" 
	    				data-options="prompt: '错误级别',url:'<%=path%>/codeSelect.do?type=errLevel',
	    				method: 'get',valueField:'value',editable:false,textField:'text',groupField:'group'" >
	    			</td>
	 			</tr>
	 			<tr>
	    			<td colspan="8" style="text-align: right;padding-right: 20px" >
						<a href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-search'" style="width:8%;" onclick="serach()">查询</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-print'"  style="width:8%;" onclick="exportByConditionAll()">导出</a>
					</td>
	    		</tr>
	    	</table>
	</form>	
</div>  
<!-- 展示部分 -->
	 <div class="easyui-panel" style="border: 0;width: 100%">
	    <table id="dg"  class="easyui-datagrid" style="width:100%;"
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="false"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			pageList="[10,20,50,200,500,1000]"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
      	<thead>
    		<tr>  
    			<th field="reportType" width="6%" align="center" halign="center">报告类型</th>
     			<th field="year" width="6%" align="center" halign="center">年度</th>
    			<th field="quarter"  width="6%" align="center" halign="center">季度</th>
    			<th field="checkedCode" width="6%" align="center" halign="center">错误级别</th>
    			<th field="temp" width="31%" align="center" halign="center">校验公式</th>
    			<th field="errorInfo" width="31%" align="center" halign="center">校验信息</th>
    			<th field="tolerance" width="6%" align="center" halign="center">容差</th>
    			<th field="checkdate" width="6%" align="center" halign="center">校验时间</th>

    	</tr>
    	</thead>
    </table>
</div>
</body>
</html>