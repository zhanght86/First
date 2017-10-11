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
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/color.css' />">
<title>投资接口数据导入</title>
<script>
function Uploader(chunk, data,callBack){
	var addWin = $('<div style="overflow: hidden;top:20px;"/>');
	var upladoer = $('<iframe/>');
	//alert(data);
	upladoer.attr({'src':'<%=basePath%>/page/interface/uploader.jsp?chunk='+ chunk + '&' + data,width:'100%',height:'100%',frameborder:'0',scrolling:'no'});
	addWin.window({
		title:"上传文件",
		height:350,
		width:550,
		minimizable:false,
		modal:true,
		collapsible:false,
		maximizable:false,
		resizable:false,
		content:upladoer,
		onClose:function(){
			var fw = GetFrameWindow(upladoer[0]);
			var files = fw.files;
			$(this).window('destroy');
			callBack.call(this,files);
		},
		onOpen:function(){
			var target = $(this);
			setTimeout(function(){
				var fw = GetFrameWindow(upladoer[0]);
				fw.target = target;
			},100);
		}
	});
}

/**
 * 根据iframe对象获取iframe的window对象
 * @param frame
 * @returns {Boolean}
 */
function GetFrameWindow(frame){
	return frame && typeof(frame)=='object' && frame.tagName == 'IFRAME' && frame.contentWindow;
}
 
function makerUpload(chunk){
	/* var year = $('#year').textbox('getValue');
	var quarter = $('#quarter').combobox('getValue');
	var yearmonth = $('#yearmonth').textbox('getValue');
	var impdate = $('#datadate').datebox('getValue');
	
	if(year == ""){
		jsutil.msg.alert('年度不能为空！');
		return false;
	}else if(quarter == ""){
		jsutil.msg.alert('季度不能为空！');
		return false;
	}else if(yearmonth == ""){
		jsutil.msg.alert('会计期间不能为空！');
		return false;
	}else if(impdate == ""){
		jsutil.msg.alert('日期不能为空！');
		return false;
	}
 */
 	var reportid = $('#reportid').textbox('getValue');
	if(reportid == ""){
		jsutil.msg.alert('报送号不能为空！');
		return false;
	}
	var array = reportid.split("_");
	var _data = 'year=' + array[0] + '&quarter=' + array[1] + '&impdate=' + array[3].substring(0,4)+'-'+array[3].substring(4,6)+'-'+array[3].substring(6,8) + '&yearmonth=' + array[3].substring(0,6);
	
	Uploader(chunk,_data,function(files){
		if(files && files.length>0){
			/* $("#res").text("成功上传："+files.join(",")); */
		}
	});
}



function reset(){
	 $('#impFrom').form('clear')
}
</script>
</head>
<body>
<div class="easyui-panel" style="padding: 10px 0px 10px 0px;">
	<form id="impFrom">
		<table cellpadding="5" width="100%">
		            <tr>
		            <td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>报&nbsp;&nbsp;送&nbsp;&nbsp;号:</label>
					</td>
					<td style="width: 300px;">
						<input id="reportid" name="reportid" class="easyui-combobox" style="width: 200px;"
						data-options="
						        prompt: '报  送  号',	
						        required:true,
						        missingMessage:'请添选择报送号',		        
							    url: '<%=path%>/codeSelect.do?type=reportid',
							    method: 'get',
							    valueField:'value',
							    textField:'text',
							    groupField:'group'
						        ">
					</td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">					</td>
					<td style="width:300px; padding-right: 40px;" align="right">
						<a href="javascript:void(0)" class="easyui-linkbutton"
							onclick="makerUpload(false)" data-options="iconCls:'icon-arrows-up'"
							style="width: 80px;">文件上传</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()"   data-options="iconCls:'icon-reload'" style="width:80px;">重置</a> 
					</td>
		            </tr>
<%-- 				<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
					</td>
					<td style="width: 300px;">
						<input id="year" name="year" class="easyui-textbox" validType="length[4,6]" data-options="prompt: '年    度',required:true,
					        missingMessage:'请添加年度'"  style="width: 200px;">
					</td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
					</td>
					<td>
						<input class="easyui-combobox" editable="false" id="quarter" name="quarter" style="width: 200px;"
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
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>会计期间：</label>
					</td>
					<td>
						<input name="yearmonth" id="yearmonth" class="easyui-textbox" validType="length[4,6]"
						data-options="prompt: '会计期间',required:true,missingMessage:'请添加会计期间'" style="width: 200px;">
					</td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期:</label>
					</td>
					<td><input id="datadate" name="datadate" class="easyui-datebox"
						data-options="prompt: '日期',required:true,missingMessage:'请添加导入日期',editable:false"
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
							onclick="makerUpload(false)" data-options="iconCls:'icon-arrows-up'"
							style="width: 80px;">文件上传</a>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()"   data-options="iconCls:'icon-reload'" style="width:80px;">重置</a> 
					</td>
				</tr> --%>
			</table>
	</form>

	<%-- <img alt="上传按钮图片" onclick="makerUpload(false)" style="width: 100px; height: 100px; margin-top: 25px;cursor: pointer;" src="${pageContext.request.contextPath }/images/upload.jpg"> --%>
</div>
<div id="res">
<font color="red">文件上传失败处理建议：将上传失败的文件打开并保存，然后再次上传。</font>
</div>
</body>
</html>