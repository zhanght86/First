<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String path=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); 
%>
<title>信息披露</title>
</head>
<body>
<%@include file="/commons/statics.jsp"%>
<script type="text/javascript" src="/js/validateyear.js"></script>
	<!-- 搜索模块 -->
	<div class="easyui-panel"  style="padding: 10px;width: 100%" >
		<form id="serachFrom" method="post" >
			<table style="width: 100%;">
	    		<tr height="30">
	    			<td style="width:10%;" >
	    				<label id="rrr">报表类型:</label>
	    			</td>
	    			<td style="width:18%;">
	    			    <input id="jj" class="easyui-combobox dillcombox show" editable="false" name="mCfReportRate" style="width: 50%;"
					       data-options="
					        prompt: '报表类别',
					        required:true,
					        missingMessage:'请添加报表类别',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=reporttype1',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
					    ">    
					    
					    <input id="mCfReportType" name="mCfReportType" type="hidden"> 
	    			</td>
	    			<td style="width:10%;" >
	    				<label>年度:</label>
	    			</td>
	    			<td style="width:18%;">
	    				<input name="mYear" class="easyui-textbox" validType="yearValidation['^[2][0-1][0-9][0-9]$','请输入合法的年份，例如：2016']" data-options="prompt: '年度',required:true" >
	    			</td>
	    		</tr>
	    		<tr  height="30">
	    			<td style="width:10%;" >
	    				<label id="rrr">季度:</label>
	    			</td>
	    			<td style="width:18%;">
						<input class="easyui-combobox  dillcombox show" editable="false" name="mQuarter" style="width: 50%;"
					    data-options="
					        prompt: '季    度',	
					        required:true,
					        missingMessage:'请添加季度',		        
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group'
					    ">
	    			</td>
	    			<td style="width:10%;" >
	    				<label>生成word类型:</label>
	    			</td>
	    			<td style="width:18%;">
	    				<select class="easyui-combobox" id="wordTypes" name="wordTypes" editable="false">
	    					<option value="0">元</option>
	    					<option value="1">万元</option>
	    				</select>
	    				<input id="wordType" name="wordType" type="hidden"> 
	    			</td>
	    			<td style="width:20%;">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="createCfWord()"  data-options="iconCls:'icon-search'" style="width:45%">生成word报告</a>
		    			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="DownCfWord()" data-options="iconCls:'icon-reload'" style="width:45%;">下载word报告</a>		
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
		<!-- <div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="createCfWord()">生成word报告</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-more" plain="true" onclick="DownCfWord()">下载word报告</a> 
			</div> -->
<!-- 	操作，展示部分 -->
	<!--  <div class="easyui-panel" style="border: 0;width: 100%">
		<table id="dg" class="easyui-datagrid"
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			title="固定因子管理"
			nowrap="true"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<thead>
				<tr>
					<th field="tableCode" width="10%" data-options="sortable:true">表代码</th>
					<th field="tableName" width="20%" data-options="sortable:true">表名称</th>
					<th field="colName" width="8%" data-options="sortable:true">列名称</th>
					<th field="outItemNote" width="25%" data-options="sortable:true">说明</th>
					<th field="reportTypeName" width="10%" >报表类型</th>
				</tr>
			</thead>
		</table>
	</div> -->
</body>
<script  type="text/javascript">
function downnn(){
}
var params = {};
$('.dillcombox').combobox({
	panelHeight:'100'
});
	function createCfWord(){
		if(!$('#serachFrom').form('validate')){
			jsutil.msg.err("请填写必要的信息");
			return;
		}
		$('#mCfReportType').val($('#jj').combobox('getValue'));
		console.log($('#mCfReportType').val());
		$('#wordType').val($('#wordTypes').combobox('getValue'));
		var url="${pageContext.request.contextPath}/cfwordCreate/createWordxinxipilu.do";
		getParams();
		$('#msg').text('正在生成，请稍后.');
		$('#dlg').dialog('open').dialog('setTitle', '文件生成');
		jsutil.core.invoke(url,params,createback);
	}
	

	function DownCfWord(){
		/* getParams();
		jsutil.core.invoke(url,params,downloadback); */
		if(!$('#serachFrom').form('validate')){
			jsutil.msg.err("请填写必要的信息");
			return;
		}
		$('#mCfReportType').val($('#jj').combobox('getValue'));
		console.log($('#mCfReportType').val());
		$('#wordType').val($('#wordTypes').combobox('getValue'));
			$('#serachFrom').form('submit',{
				url:'${pageContext.request.contextPath}/cfwordCreate/downloadxinxipilu.do',
				success: function(result){
					var data=eval("("+result+")")
						if(data.success){
							
						}else{
							jsutil.msg.err(data.errorMsg);
						}
				}
			});
	}
	function serach(){
		getParams();
		console.log($('#dg').datagrid('options'));
		;
		$('#dg').datagrid('load', params);
	}
	function reset(){
		$('#serachFrom').form('clear');
	}
	function getParams(){
		$('#serachFrom').find('input').each(function(){
	        var obj = $(this);
	        var name = obj.attr('name');
	        if(name){
	        	var value=obj.val();
	        	if(value!="")
	            params[name] = obj.val();
	        }
	    });
	}
	function createback(result){
		$('#dlg').dialog('close');
		if(result.success){
			jsutil.msg.alert("成功生成文件");
		}else{
			jsutil.msg.alert(result.errorMsg);
		}
	}
	function downloadback(result){
		if(result.success){
			
		}else{
			jsutil.msg.alert(result.errorMsg);
		}
	}
	
	var loading
	function onOpen(){
		 loading=setInterval(showalert, 500); 
	}
	var i=2;
	function showalert() 
	{ 
		var text="";
		if (i == 1) {
			text = '正在生成，请稍后.';
		} else if (i == 2) {
			text = '正在生成，请稍后..';
		} else if (i == 3) {
			text = '正在生成，请稍后...';
			i = 0;
		}
		i++;
		$('#msg').text(text);
	} 
	function onClose(){
		clearInterval(loading); 
	}
</script>
</html>