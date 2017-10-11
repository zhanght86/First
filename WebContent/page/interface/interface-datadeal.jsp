<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>接口数据处理</title>
<script type="text/javascript">
function deal(){	
	$('#dealFrom').form('submit',{
		url: '${pageContext.request.contextPath}/InterDeal/datadeal.do',
 		onSubmit: function(){
 			return checkinput();
 		},
		 success: function(result){
			$('#dlg').dialog('close')
			var result = eval('('+result+')');
			if (!result.success){
				jsutil.msg.err('处理失败:'+result.errorMsg)
			} else {
				jsutil.msg.alert('处理成功')
			}
		} 
	});
}

function checkinput(){
	if($('#dealFrom').form('validate')){
		$('#msg').text('正在处理，请稍后.');
		$('#dlg').dialog('open').dialog('setTitle','数据处理');
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
function showalert(){ 
		var text="";
		if(i==1){
			text='正在处理，请稍后.';
		}else if(i==2){
			text='正在处理，请稍后..';
		}else if(i==3){
			text='正在处理，请稍后...';
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
	$('#dealFrom').form("clear");
}
</script>
</head>
<body>
	<div class="easyui-panel" style="padding: 10px 0px 10px 0px;">
		<form  id="dealFrom" method="post">
			<table cellpadding="5" width="100%">
				
				<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>报&nbsp;送&nbsp;号&nbsp;:</label>
					</td>
					<td style="width: 300px;">
						<input id="reportid" name="reportid" class="easyui-combobox" style="width: 200px;"
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
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期:</label>
					</td>
					<td><input id="impdate" name="impdate" class="easyui-datebox"
						data-options="prompt: '取数日期',editable:false,required:true,missingMessage:'请添加季度',"
						style="width: 200px;"></td>
				</tr>
				<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
					</td>
					<td style="width: 300px;">
						</td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
					</td>
					<td style="width:280px; padding-right: 40px;" align="right">
						<a href="javascript:void(0)" class="easyui-linkbutton"
							onclick="deal()" data-options="iconCls:'icon-ok'"
							style="width: 80px;">数据处理</a>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()"   data-options="iconCls:'icon-reload'" style="width:80px;">重置</a> 
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="dlg" class="easyui-dialog"
		style="width: 300px; height:100px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,onOpen:onOpen,onClose:onClose">
		<div id="msg"></div>
    </div>
</body>
</html>