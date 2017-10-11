<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>投资数据导入</title>
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
  $(function() {  	
  // 下拉框选择控件，下拉框的内容是动态查询数据库信息
  $('#interfacetype').combobox({ 
	  prompt: '数据类型',
      required:true,
      missingMessage:'请添加数据类型',
	  url: '${pageContext.request.contextPath}/codeSelect.do?type=investinterfacetype',
	  method: 'get',
	  valueField:'value',
	  textField:'text',
	  groupField:'group',
          
  onHidePanel: function(){
        $("#interfacetable").combobox("setValue",'');
      var investInterfaceType = $('#interfacetype').combobox('getValue');
      
      $.ajax({
        type: "GET",
        url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=investinterfacetable&params='+investInterfaceType,
        cache: false,
        dataType : "json",
        success: function(data){
        $("#interfacetable").combobox("loadData",data);
                 }
            }); 	
          }
             }); 
  
  $('#interfacetable').combobox({ 
      prompt: '文件名称',
      required:true,
      missingMessage:'请添加文件名称',
	  valueField:'value',
	  textField:'text',
     });
  
  });
</script>

<body style="width: 100%;height: 100%;overflow:hidden;margin: 0;padding: 0;">
    <br><br>
	<h1>投资数据导入</h1>
		<div class="easyui-panel" style="padding: 10px 0px 10px 0px;">
		<form id="uploadForm" method="post" enctype="multipart/form-data" action="<%=path%>/cux_sino/impinvestdata.do">
		<table cellpadding="5">
	    		<tr>
	    			<td style="width:80px;text-align:right;padding-right: 40px;" >数据类型:</td>
	    			<td style="width:300px;">
	    			    <input class="easyui-combobox" editable="false" id="interfacetype" name="interfacetype" style="width: 200px;">
					</td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >文件名称:</td>
	    			<td style="width:300px;">
	    			    <input class="easyui-combobox" editable="false" id="interfacetable" name="interfacetable" style="width: 200px;">
					</td>
	    		</tr>
	    		<tr>
	    			
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >年&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp度:</td>
	    			<td>
	    				<input name="year" class="easyui-textbox" validType="length[4,4]" data-options="prompt: '年    度',required:true,
					        missingMessage:'请添加会计月度'"  style="width: 200px;">
	    			</td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >季&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp度:</td>
	    			<td style="width:300px;">
	    			    <input class="easyui-combobox" editable="false" name="quarter" style="width: 200px;"
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
	    		<tr>	    			
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >文件地址:</td>
	    			<td>
	    				<input class="easyui-filebox" name="file" data-options="prompt: '文件路径',required:true,
					        missingMessage:'请添加文件路径'" style="width: 200px;">
	    			</td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >截止日期:</td>
	    			<td>
	    				<input class="easyui-datebox" name="datadate" style="width: 200px;">
	    			</td>
	    		</tr>
<!-- 	    		<tr>	    			
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >备&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp注:</td>
	    			<td>
	    				<input class="easyui-textbox" name="remark" style="width: 200px;">
	    			</td>
	    		</tr> -->
	    		<tr>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" ></td>
	    			<td style="width:300px;text-align: right;padding-right: 40px;" ></td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" ></td>
	    			<td colspan="2" style="width:400px;text-align: right;padding-right: 40px;" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="ok()"  data-options="iconCls:'icon-print'" style="width:80px;">上传数据</a>
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
</body>
<script  type="text/javascript">
//ok
function ok(){	
	if(checkfiletype()){		
	$('#uploadForm').form('submit',{
		url: '${pageContext.request.contextPath}/cux_sino/impinvestdata.do',
 		onSubmit: function(){
 			return checkinput();
 		},
		 success: function(result){
			$('#dlg').dialog('close')
			var result = eval('('+result+')');
			if (result.errorMsg){
				jsutil.msg.err('上传失败'+result.errorMsg)
			} else {
				jsutil.msg.alert('上传成功')
			}
		} 
	});
	}else{
		jsutil.msg.warn('上传文件未选择或上传文件类型错误！')
	}
}

function checkinput(){
	if($('#uploadForm').form('validate')){
		$('#msg').text('正在上传，请稍后.');
		$('#dlg').dialog('open').dialog('setTitle','文件上传');
        return true;
	}else{
		return false;
	}
}
//校验上传文件格式
function checkfiletype(){
	var file = $("input[name='file']").val();
	//获取扩展名  
    var ext = file.substring(file.lastIndexOf(".")+1);  
    if(ext=="xls"||ext=="xlsx"||ext=="xlsm"){
    	return true;
    }else{
    	return false;
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
		if(i==1){
			text='正在上传，请稍后.';
		}else if(i==2){
			text='正在上传，请稍后..';
		}else if(i==3){
			text='正在上传，请稍后...';
			i=0;
		}
		i++;
		$('#msg').text(text);
} 
function onClose(){
	clearInterval(loading); 
}
//重置
function reset(){
	$('#uploadForm').form("clear");
}
</script>
</html>