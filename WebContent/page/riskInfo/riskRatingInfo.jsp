<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>风险综合评级数据上传</title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@include file="/commons/statics.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/color.css' />">
<script type="text/javascript" src="/js/validateyear.js"></script>

<script type="text/javascript">
    /* 初始化下载表格信息 */
  $(function() {  	
//		alert($('#comCode1').val());

  $('#comCode').combobox({ 
      prompt: '机构名称',	
      required:true,
      missingMessage:'请添加机构名称',		        
	  url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=comCode&params=${currentUser.comCode}',
	  method: 'get',
	  valueField:'value',
	  textField:'text',
	  value:'${currentUser.comCode}',
	  groupField:'group'
  }); 
  
  });
</script>
</head>


<body style="width: 100%;height: 100%;overflow:hidden;margin: 0;padding: 0;">
		<div style="padding: 10px 10px 10px 10px;">
		<fieldset>
			
		<form id="uploadForm" method="post" enctype="multipart/form-data" action="<%=path%>/RiskRatingCheck/check.doo">
		<table cellpadding="5" style="width: 100%;" >
	    		<tr height="30">
	    			<td  style="width:10%;">
		    				<label>年&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp度:</label>
		    		</td>
	    			<td style="width:30%;">
	    				<input name="year" class="easyui-textbox"  data-options="prompt: '年    度',required:true,
					        missingMessage:'请添加会计月度'"  validType="yearValidation['^[2][0-1][0-9][0-9]$','请输入合法的年份，例如：2016']" style="width:60%;">
	    			</td>
	    			
	    			<td  style="width:10%;">
		    				<label>季&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp度:</label>
		    		</td>
	    			<td style="width:30%;">
	    			    <input class="easyui-combobox" editable="false" name="quarter" style="width: 60%;"
					    data-options="
					        prompt: '季    度',	
					        required:true,
					        missingMessage:'请添加季度',		        
						    url: '<%=path%>/codeSelect.do?type=quarter',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group'
					    ">
					</td>
					
					
	    			</tr>
	    			
	    			<tr height="30">
	    			<td  style="width:10%;">
		    				<label>机构名称:</label>
		    		</td >
	    			<td style="width:30%;">
	    				<input class="easyui-combobox" editable="false" name="comCode" id="comCode" style="width: 60%;">
	    			</td>
	    			
	    			<td style="width:10%;" >
		    			</td>
		    			<td style="width:30%;">
		    			</td>
		    			
					<td style="width:40%;" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchInfo()"  data-options="iconCls:'icon-print'" style="width:80px;">查询</a>	   
	    				<a href="javascript:void(0)" id="exportbtn" class="easyui-linkbutton" onclick="exportXls('dg1','数据校验')" data-options="iconCls:'icon-print'" style="width:80px;">导出</a> 

	    			</td>
	    		</tr>


	    	</table>
	</form>
	</fieldset>	
</div>

	<!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog"
		style="width: 300px; height:100px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,onOpen:onOpen,onClose:onClose">
		<div id="msg"></div>
    </div>
    <br>
    <br>
    
    <input type="text" style="display:none" id="comCode1" closed="true" value="${currentUser.comCode}" />
    
<!-- 展示部分 -->
<div class="easyui-panel"  style="border: 0;width: 100%; height:366px;" id="dataDiv">
	    <table id="dg1"  class="easyui-datagrid" style="width:100%;"
	         url=${pageContext.request.contextPath}/RiskRatingCheck/list.do
			rownumbers="true"
			fitColumns="true"
			singleSelect="true"
			pagination="true" 
			striped="true"
			title="校验结果信息"
			nowrap="true"
			autoRowHeight="false"
			style="width:90%;height:220px;"
			loadMsg="加载数据中..."
			>
			
      	<thead>
    		<tr>  
    		    <th field="comCode" align="center" width="25" >公司名称</th>
     			<th field="year"  align="center" width="25" >年度</th>
    			<th field="quarter"  align="center" width="25" >季度</th>
    			<th field="reporttype" align="center" width="25" >报告类型</th>
    			<th field="errLevel" align="center" width="25" >错误级别</th>    			
    			<th field="temp" width="18" align="center" width="25" >校验公式</th>
    			<th field="errorInfo" width="18" align="center" width="25" >校验信息</th>
    			 <th field="checkdate"  align="center" width="25" >校验时间</th>

    	</tr>
    	</thead>
    </table>
 </div>
</body>
<script  type="text/javascript">


function onOpen(){
	 loading=setInterval(showalert, 500); 
}
function onClose(){
	clearInterval(loading); 
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

function searchInfo(){
	
	$('#msg').text('正在查询，请稍后.');
	
	var params = {};
	$('#uploadForm').find('input').each(function(){
       var obj = $(this);
       var name = obj.attr('name');
       if(name){
           params[name] = obj.val();
       }
   }); 	

	$('#dg1').datagrid("load", params);
	
} 

function exportXls(gridId, xlsName) {
	var param=jsutil.core.exportXls(gridId,xlsName);
	jsutil.core.download("${pageContext.request.contextPath}/downloadExcel",param);
}
</script>
</html>