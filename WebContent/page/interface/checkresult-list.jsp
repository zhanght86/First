<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检验结果查询页面</title>
<script type="text/javascript" src="/js/validateyear.js"></script>
<script  type="text/javascript">
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
	
	$('#dg').datagrid("load", params);
	
	$('#dg').datagrid({
		//加载成功
		onLoadSuccess: function(data){
			if (data.total == 0){
				//$("#dg").datagrid('appendRow',{year:'没有符合条件的查询结果'});
				jsutil.msg.alert('没有符合条件的查询结果');
			} 
			
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
<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
		<form id="serachFrom" method="post">
			<table style="width: 100%;">
		
	    		<tr>
	    			<td style="width:10%;">
	    				<label>校验编码:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input class="easyui-combobox" id="reporttype" name="reporttype" style="width:60%;"
					data-options="
						url: '<%=path%>/codeSelect.do?type=reporttype',
						prompt:'报告类型',
						method: 'get',
						valueField:'value',
						editable:false,
						textField:'text'">
	    			</td>
	    			 <td style="width:10%;" >
	    				<label>校验状态:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input  id="checkedCode"   name="checkedCode" class="easyui-combobox"  style="width: 200px;" 
	    				data-options="prompt: '错误级别',
	    				url:'<%=path%>/codeSelect.do?type=errLevel',
	    				method: 'get',
						valueField:'value',
						editable:false,
						textField:'text',
						groupField:'group'" >
	    			</td>
	    		  
	    		</tr>
	    		
	    		<tr>
	    			 <td style="width:10%;" >
	    				<label>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input  id="year" name="year" class="easyui-textbox" validType="yearValidation['^[2][0-1][0-9][0-9]$','请输入合法的年份，例如：2016']" data-options="prompt: '年度'"  style="width:60%;">
	    			</td>
	    			<td style="width:10%;">
	    				<label>季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input  id="quarter"   name="quarter"  class="easyui-combobox"  style="width:60%;"  
	    				data-options="
	    				prompt: '季度', 
	    				url:'<%=path%>/codeSelect.do?type=quarter',
	    				method: 'get',
						valueField:'value',
						editable:false,
						textField:'text',
						groupField:'group'" >
	    			</td>
	    			
	    			
	 
	    			<td style="width:40%;" >
						<a href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-search'" style="width:80px;" onclick="serach()">查找</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-print'"  style="width:80px;" onclick="exportXls('dg','数据校验结果')">导出</a>
					</td>
	    		</tr>
	    	</table>
		
	</form>	
</div>  
<!-- 展示部分 -->

	    <table id="dg"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/dataCheck/list.do
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			pageList="[10,20,50,200,500,1000]"
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
      	<thead>
    		<tr>  
    				<th field="year">年度</th>
					<th field="quarter">季度</th>
					<th field="checkdate">校验编码</th><!-- filed是错误的,需要修改 -->
					<th field="temp">校验公式</th>
					<th field="checkdate">校验时间</th>
					<th field="checkdate">校验状态</th><!-- filed是错误的,需要修改 -->
					<th field="checkdate">数据来源</th><!-- filed是错误的,需要修改 -->
    	</tr>
    	</thead>
    </table>
</body>
</html>