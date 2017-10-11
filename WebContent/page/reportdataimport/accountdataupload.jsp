<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>完整数据管理</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@include file="/commons/statics.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/color.css' />">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
</head> 

<body >
<!-- 搜索模块 -->
<!-- 搜索模块 -->
<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form id="serachFrom" method="post" style="margin-bottom: 0">
			<div style="display: none;">
				<input type="hidden" name="department" id="department" class="easyui-textbox" value="01">
				<%--modelType区分 空模板和数据模板 --%>
				<input type="hidden" name="modelType" id="modelType" class="easyui-textbox">
			</div>
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">
		    		<tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>报送号:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input  style="width:100%;" id="reportId" name="reportId" class="easyui-textbox" data-options="prompt: '报送号'"  >
		    			</td>
			    		<td style="width:10%;text-align: right;" >
		    				<label>报送年度:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<!-- <input style="width:100%;" id="year" name="year" class="easyui-textbox" data-options="prompt: '报送年度'"  > -->
		    				<input style="width: 100%;" id="year" name="year" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
		    			</td>
		    			 <td style="width:8%;text-align: right;" >
		    				<label>报送季度:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="quarter" name="quarter" class="easyui-combobox" editable="false" 
		    				 data-options=" prompt: '报送季度', missingMessage:'请添加季度',
						     url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
						     method: 'get', valueField:'value',textField:'text', groupField:'group'">
		    			</td>
		    			<td style="width:8%;text-align: right;" >
		    				<label>报送类型:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="reportType" name="reportType" class="easyui-combobox" editable="false" 
		    				data-options=" prompt: '报表类别', missingMessage:'请添加报表类别',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=reporttype',
						    method: 'get', valueField:'value', textField:'text',groupField:'group'">
		    			</td>
		    			
				    </tr>
				    <tr>
				    <!--
				    <td style="width:8%;text-align: right;" >
		    				<label>报送状态:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input style="width:100%;" id="reportState" name="reportState" class="easyui-combobox"  
		    				data-options=" prompt: '报送状态',editable:'false',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=cfreportstate',
						    method: 'get',valueField:'value',textField:'text', groupField:'group'">
		    			</td>
		    			<td style="width:13%;text-align: right;" >
		    				<label>是否完成手工导入:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<select style="width:100%;"  id="handleFlag" name="handleFlag" class="easyui-combobox" data-options="prompt: '是否完成手工导入'"  editable="false" >
		    					<option value=""></option>
		    					<option value="0">否</option>
		    					<option value="1">是</option>
		    				</select>
		    			</td>
		    			-->
		    			
			    		
				    </tr>
				    <tr>
	    				<td colspan="8" style="text-align: right; padding-right: 20px" >
		    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">查询</a>
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
	 <div class="easyui-panel" style="border: 0;width: 100%">
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
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="modeldownloaddate()"  plain="true" data-options="iconCls:'icon-print'" >完整数据下载</a>
   				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="modedownload()"  plain="true" data-options="iconCls:'icon-print'" >模板下载</a>
   				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="makerUpload(false)"  plain="true" data-options="iconCls:'icon-print'" >上传文件</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="26%" align="center" halign="center" >报送号</th>
					<th field="year" width="19%" align="center" halign="center">报送年度</th>
					<th field="quarter" width="14%" hidden>报送季度</th>
					<th field="quarterName" width="18%" align="center" halign="center" >报送季度</th>
					<th field="reportType" width="10%" hidden>报送类型</th>
					<th field="reportTypeName" width="18%" align="center" halign="center" >报送类型</th>
					<th field="reportCategory" width="15%" align="center" halign="center"
						formatter="resultCategory"
						editor="{type:'combobox',options:{
								editable:false,
								valueField:'value',
								textField:'text',
								method:'get'
							}}">报送类别</th>
					<!--
					<th field="reportState" width="12%" hidden>报送状态</th>
					<th field="reportStateName" width="14%"  align="center" halign="center">报送状态</th>
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
    <div id="dlg2" class="easyui-dialog" style="width:850px;height:450px;"
	    		top=20px buttons="#dlg-buttons" data-options="resizable:true,iconCls:'icon-more',modal:true,closed:true,onClose:cancel1" title="反馈">
    	<table id="dg2" class="easyui-datagrid"
			rownumbers="true"
			fitColumns="true"
			singleSelect="true"
			pagination="false" 
			striped="true"
			title="错误结果信息"
			nowrap="false"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			autoRowHeight="false"
			loadMsg="加载数据中..."
			>
			<thead>
				<tr>
					<th field="reportcode" halign="center"  align="left" width="13">因子代码</th>
					<th field="reportname" halign="center"  align="left" width="23">因子名称</th>
					<th field="filename" halign="center" align="left" width="21">所属表格名称</th>
					<th field="remark" halign="center" align="left" width="13">错误原因</th>
					<th field="result" halign="center" align="left" width="18" >结果</th>
			</thead>
		</table>
    </div>
    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton showbutton" iconCls="icon-cancel" onclick="cancel();">关闭</a>
    	<a href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-print'" onclick="exportXls('dg2','报送数据错误结果信息')">导出</a>
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
/**
 * 根据iframe对象获取iframe的window对象
 * @param frame
 * @returns {Boolean}
 */
function GetFrameWindow(frame){
	return frame && typeof(frame)=='object' && frame.tagName == 'IFRAME' && frame.contentWindow;
}
 
function makerUpload(chunk){
	var rows = $('#dg').datagrid('getSelections');	
	if(rows.length==0||rows.length>1){
		$.messager.alert('消息提示','请选择一条报送号进行文件上传!','error');
		return ;
	}
	
	endEditing();
	var year = rows[0].year;
	var quarter = rows[0].quarter;
	var reportId = rows[0].reportId;
	var reportname=rows[0].reportCategory;
	var reporttype=rows[0].reportTypeName;
	
	if(reportname==""||reportname==null){
		$.messager.alert('消息提示','请选择报送类别!','error');
		return false;
	}
	if(reporttype=='季度快报'){
		reporttype=1;
	}
	if(reporttype=='季度报告'){
		reporttype=2;
	}
	if(reporttype=='临时报告'){
		reporttype=3;
	}
	
	var _data = 'year=' + year + '&quarter=' + quarter +'&reportId=' + reportId+'&reportname='+reportname+'&reporttype='+reporttype ;
	//alert(_data);
	Uploader(chunk,_data,year,quarter,reportId,reportname,reporttype,function(files){
		if(files && files.length>0){
			 $("#res").text("成功上传："+files.join(",")); 
		}
	});
}

function Uploader(chunk,_data, year,quarter ,reportId,reportname,reporttype, callBack){
	var addWin = $('<div style="overflow: hidden;top:20px;"/>');
	var upladoer = $('<iframe/>');

	upladoer.attr({'src':'<%=basePath%>page/reportdataimport/multiupload.jsp?chunk2='+ chunk + '&' + _data,width:'100%',height:'100%',frameborder:'0',scrolling:'no'});
	addWin.window({
		title:"上传文件",
		height:370,
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


function resultCategory(value,row){
	return row.value;
}
var editIndex = undefined;
function endEditing(){
	if (editIndex == undefined){return true}
	if ($('#dg').datagrid('validateRow', editIndex)){
		var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:'reportCategory'});
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
	
	var row =$('#dg').datagrid('getSelected');
	var ed = $('#dg').datagrid('getEditor', {index:index,field:'reportCategory'});
	if(ed!=null){
		var tar = ed.target;
		//$(tar).combobox('clear');
		$(tar).combobox('reload','${pageContext.request.contextPath}/codeSelect/hasParam.do?type=reportnameTy&params='+row.reportType);
	}
}
//ok
function ok(){	
	var rows = $('#dg').datagrid('getSelections');	
	endEditing();
	if(rows[0].reportCategory==""||rows[0].reportCategory==null){
		$.messager.alert('消息提示','请选择报送类别!','error');
		return false;
	}
	var data =[{name:'reportId',value:rows[0].reportId },{name:'reportname',value:rows[0].reportCategory }];
	if(checkfiletype()){		
		$('#uploadForm').form('submit',{
			url: '${pageContext.request.contextPath}/upload/reportupload.do',
	 		onSubmit: function(){
	 			type = 0;
	 			return checkinput();
	 		},
			 success: function(result){
				var result = eval('('+result+')');
				if (result.errorMsg){
					jsutil.msg.err('上传失败'+result.errorMsg)
				} else if(result.data != null){
					$.messager.alert("警告","上传失败，数据格式异常信息如下!","warning",function(){
						$("#dg").datagrid({data:result.data});
						$("#dlg2").dialog('open');
					});
					//console.log(result.data);
				}else {
					type = 0;
					$('#dlg').dialog('open').dialog('setTitle','数据上传');
					$('#msg').text('正在上传数据，请稍后.'); 
					endEditing();
					jsutil.msg.alert('上传成功')
					$('#dlg').dialog('close');
				}
			} 
		});
		}else{
			jsutil.msg.warn('上传文件未选择或上传文件类型错误！')
		}
	}	
function checkinput(){
	if(type == 0){
		//if ($('#uploadForm').form('validate')) {
			$('#msg').text('正在上传，请稍后.');
			$('#dlg').dialog('open').dialog('setTitle', '文件上传');
			return true;
		//} else {
			//return false;
		//}
	}else if(type == 1){
		$('#msg').text('正在下载，请稍后.');
		$('#dlg').dialog('open').dialog('setTitle', '文件下载');
		return true;
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
{ //type=0;
		var text="";
		if(type == 0){
			if (i == 1) {
				text = '正在上传，请稍后.';
			} else if (i == 2) {
				text = '正在上传，请稍后..';
			} else if (i == 3) {
				text = '正在上传，请稍后...';
				i = 0;
			}
		}else if(type == 1){
			if (i == 1) {
				text = '正在下载，请稍后.';
			} else if (i == 2) {
				text = '正在下载，请稍后..';
			} else if (i == 3) {
				text = '正在下载，请稍后...';
				i = 0;
			}
		}
		i++;
		$('#msg').text(text);
} 
function onClose(){
	clearInterval(loading); 
}
//模板下载
function modedownload(){
	var rows = $('#dg').datagrid('getSelections');	
	if(rows.length==0||rows.length>1){
		$.messager.alert('消息提示','请选择一条报送号进行模板下载!','error');
		return ;
	}
	endEditing();
	if(rows[0].reportCategory==""||rows[0].reportCategory==null){
		$.messager.alert('消息提示','请选择报送类别!','error');
		return false;
	}
	type = 1;
	$('#dlg').dialog('open').dialog('setTitle','数据下载');
	$('#msg').text('正在下载数据，请稍后.');
	var data =[{name:'reportId',value:rows[0].reportId },{name:'yearmonth',value:rows[0].year},{name:'quarter',value:rows[0].quarter},{name:'reporttype',value:rows[0].reportType },{name:'reportname',value:rows[0].reportCategory }];
	$.post('${pageContext.request.contextPath}/upload/modedownload.do',data,function(result){
		$('#dlg').dialog('close')
		if (result.success){
			location.href = '${pageContext.request.contextPath}/downLoadReport/downLoad.do?filepath='+result.data;			
		} else {
			jsutil.msg.err('下载失败'+result.errorMsg);
		}
	},'json'); 
}
//模板下载
/* function modedownload(){			
	$('#uploadForm').form('submit',{
		url: '${pageContext.request.contextPath}/upload/modedownload.do',
 		onSubmit: function(){
 			type = 1;
			return checkinput();
 		},
		 success: function(result){
			$('#dlg').dialog('close')
			var result = eval('('+result+')');
			if (result.errorMsg){
				jsutil.msg.err('下载失败'+result.errorMsg)
			} else {
				jsutil.msg.alert('下载成功')
			}
		} 
	});
} */
//完整数据下载
function modeldownloaddate(){	
	var rows = $('#dg').datagrid('getSelections');	
	if(rows.length==0||rows.length>1){
		$.messager.alert('消息提示','请选择一条报送号进行完整数据下载!','error');
		return ;
	}
	endEditing();
	if(rows[0].reportCategory==""||rows[0].reportCategory==null){
		$.messager.alert('消息提示','请选择报送类别!','error');
		return false;
	}
	type=1;
	$('#dlg').dialog('open').dialog('setTitle','数据下载');
	$('#msg').text('正在下载数据，请稍后.');
	console.log(rows[0].reportId);
	var currDate =Date.parse(new Date());
	var data =[{name:'reportId',value:rows[0].reportId },{name:'yearmonth',value:rows[0].year},{name:'quarter',value:rows[0].quarter},{name:'reporttype',value:rows[0].reportType },{name:'reportname',value:rows[0].reportCategory },{name:'currDate',value:currDate}];
	$.post('${pageContext.request.contextPath}/commonImportDate/modeldownloaddate.do',data,function(result){
		if (result.success){
		$('#dlg').dialog('close')
			location.href = '${pageContext.request.contextPath}/downLoadReport/downLoad.do?filepath='+result.data;			
		} else {
			jsutil.msg.err('下载失败'+result.errorMsg);
		}
	},'json'); 
}
/* //完整数据下载
function modeldownloaddate(){
	//$.blockUI({ message: '正在下载,请稍后...' });
	var rows = $('#dg').datagrid('getSelections');
	var data =[{name:'reportId',value:rows[0].reportId },{name:'reportname',value:rows[0].reportCategory }];
	$.post('${pageContext.request.contextPath}/commonImportDate/modeldownloaddate.do',data,function(result){
		if (result.success){		
			 $('#dlg').dialog('close');
			 location.href = result.data;}
		else {
			jsutil.msg.err('下载失败'+result.errorMsg);
		}
			 
	});
} */
//重置
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
		url:'${pageContext.request.contextPath}/calCfReportInfo/listpage.do?reportCateGory=0',
	     	queryParams: params,
			onBeforeLoad: function(){
				if(!$('#serachFrom').form('validate')) return false;
			},
			onLoadSuccess: function(data){
				if(data.total>0) {
					editIndex = undefined;
					endEditing();
	 				return false;
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
function exportXls(gridId,xlsName){
	  $("#dlg2").dialog('close');
	  var param = jsutil.core.exportXls(gridId, xlsName);
	  jsutil.core.download("${pageContext.request.contextPath}/downloadExcel",param);
	 
}  
//打开错误信息框
function openDialog(data){
	 //console.log(data);
	$("#dg2").datagrid({data:data});
	$("#dlg2").dialog('open');
}
//关闭错误信息框
function cancel(){
		$('#dlg2').dialog('close');
}
//关闭导入窗口
function cancel1(){
		$('iframe')[0].contentWindow.closeWindow();
}

/* $(function (){
	$('#dg').datagrid({
		//加载成功
		onLoadSuccess: function(data){
			if(data.total>0){
				return false;
			}
			$.messager.alert("提示","未查询到相关数据","info");
		}
	});
}) */
</script>
</html></html>