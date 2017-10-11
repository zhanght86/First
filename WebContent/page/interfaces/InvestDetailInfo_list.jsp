<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<title>投资接口信息管理</title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@include file="/commons/statics.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/color.css' />">
</head>
<script type="text/javascript">
    /* 初始化下载表格信息 */
  /*$(function() {  	
  // 下拉框选择控件，下拉框的内容是动态查询数据库信息
  $('#interfacetype').combobox({ 
	  prompt: '数据类型',
      required:true,
      missingMessage:'请添加数据类型',
	  url: '${pageContext.request.contextPath}/codeSelect.do?type=InvestDataType',
	  method: 'get',
	  valueField:'value',
	  textField:'text',
	  groupField:'group'
             });  
  
  var interfaceType = $("input[name='interfacetype']").val();
  if(interfaceType=="投资资产明细表"){
	 $("#divXT").hide();
	 $("#divTZ").show();
  }else if(interfaceType=="固定收益类信托计划明细表"){
	 $("#divTZ").hide();
	 $("#divXT").show();
  }  
  });
    */
   
</script>
<body>
	<!-- 搜索模块 -->
	<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
		<form id="serachFrom" method="post">
		<table style="width: 100%;">
							    		
		    		<tr height="30">
						<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>报&nbsp;送&nbsp;号&nbsp;:</label>
					</td>
					<td style="width: 300px;">
						<input id="reportId" name="reportId" class="easyui-combobox" style="width: 200px;"
						data-options="
						        prompt: '报  送  号',	
						        required:true,
						        missingMessage:'请添选择报送号',		        
							    url: '<%=path%>/codeSelect.do?type=reportid_import',
							    method: 'get',
							    valueField:'value',
							    textField:'text',
							    groupField:'group'
						        ">
					</td>
				    </tr>	
				<!--   	    		
		    		<tr>
						<td style="width:10%;" >
		    				<label>数据类型:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="interfacetype" name="interfacetype" class="easyui-combobox" data-options="prompt: 'interfacetype',required:true,
					        missingMessage:'请先填写数据类型'" value="投资资产明细表" style="width:60%;">
		    			</td>							
					</tr>
					-->   
	    			<tr height="30">
		    			<td style="width:10%;" >
		    				<span  style="width:10%;" ></span>
		    			</td>
		    			<td style="width:30%;">
		    				<span  style="width:10%;" ></span>
		    			</td>
		    			<td style="width:10%;" >
		    				<span  style="width:10%;" ></span>
		    			</td>
		    			<td style="width:30%;">
		    				<span  style="width:10%;" ></span>
		    			</td>
		    			<td style="width:40%;" >
		    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">查询</a>
		    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
		    			</td>
	    			</tr>
	    	</table>
		</form>
	</div>
<!-- 	操作，展示部分 -->
	<div id="divTZ">
		<h1> 投资资产明细表</h1>
		<table id="dgTZ" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/interTabInvestInfo/listpage.do
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="false" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			style="width:150%;height:auto;"
			loadMsg="加载数据中..." 
			>
			
			<thead>
				<tr>
					<th field="year" width="25" sortable="true">年度</th>
					<th field="quarter" width="25" sortable="true">季度</th>
					<th field="reportDate" width="25" sortable="true">报送日期</th>
					<th field="reportId" width="25" sortable="true">报送号</th>
					<th field="accounts" width="25" sortable="true">账户</th>
					<th field="properCode" width="25" sortable="true">债券编码</th>
					<th field="properName" width="25" sortable="true">债券名称</th>
					<th field="issurer" width="25" sortable="true">发行人</th>
					<th field="creditLevel" width="25" sortable="true">信用评级</th>
					<th field="fixedDuration" width="25" sortable="true">修正久期</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
<script  type="text/javascript">

//	重置
function reset(){
	$('#serachFrom').form("clear");
}

//校验输入项
/**
 * 
 function checkInput(){
	
	var interfaceType = $("input[name='interfacetype']").val();
	if(interfaceType==""||interfaceType==null){
		alert("请先填写数据类型！");
		return false;
	} 
	return true;
  }*/
 
//搜索
function serach(){
	
		displayData();
		
}
//选择数据类型后显示不同的列表
  /**$("#interfacetype").combobox({
	onChange:function(){
		 var interfaceType = $("input[name='interfacetype']").val();
		  if(interfaceType==1){
			  $("#divXT").hide();
			  $("#divTZ").show();
			 displayData();
		  }else if(interfaceType==2){
			  $("#divXT").show();
			  $("#divTZ").hide();
			  $('#dgXT').datagrid('reload'); 
			  displayData();
		  }
	}
});*/
function displayData(){
	var params = {};
	$('#serachFrom').find('input').each(function(){
        var obj = $(this);
        var name = obj.attr('name');
        if(name){
            params[name] = obj.val();
        }
    });
		$('#dgTZ').datagrid("reload", params);
		
}
</script>
</html>