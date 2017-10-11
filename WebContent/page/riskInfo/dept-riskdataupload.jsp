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
<script type="text/javascript" src="/js/validateyear.js"></script>

<script  type="text/javascript">
$(function(){
	$('#year').combobox({
	    disabled:false,
	    prompt:'年度',
	    valueField:'value',
	    textField:'text',
	    url:'${contextPath}/json/year.json'
	});
});
$(function(){
	serach();
})
//重置
function reset(){
	$('#serachFrom').form("clear");
	$('#dataDiv').panel('close');
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
	//$('#dgs').datagrid("load", params);	
	$("#dgs").datagrid({
		url:'${pageContext.request.contextPath}/calCfReportInfo/listpage.do?reportCateGory=1',
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

/* 初始化下载表格信息 */
$(function() {  	
// 下拉框选择控件，下拉框的内容是动态查询数据库信息
$('#reporttype').combobox({ 
	  prompt: '报表类别',
    required:true,
    missingMessage:'请添加报表类别',
	  url: '${pageContext.request.contextPath}/codeSelect.do?type=reporttype',
	  method: 'get',
	  valueField:'value',
	  textField:'text',
	  groupField:'group',
	  value:'2',
}); 

$('#quarter').combobox({ 
    prompt: '季    度',	
    missingMessage:'请添加季度',		        
	  url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
	  method: 'get',
	  valueField:'value',
	  textField:'text',
	  groupField:'group'
}); 

$('#companycode').combobox({ 
    prompt: '机构名称',	
    required:true,
    missingMessage:'请添加机构名称',		        
	  url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=companycode&params=${currentUser.comCode}',
	  method: 'get',
	  valueField:'value',
	  textField:'text',
	  value:'${currentUser.comCode}',
	  groupField:'group'
}); 

$('#reportname').combobox({ 
    prompt: '报表名称',
    required:true,
    missingMessage:'请添加报表名称',
    url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=riskreportname&params=${currentUser.comCode}',
	  valueField:'value',
	  textField:'text',
	  method: 'get',
	  groupField:'group',
   });
});
function exportXls(gridId,xlsName){
	  $("#dlg2").dialog('close');
	  var param = jsutil.core.exportXls(gridId, xlsName);
	  jsutil.core.download("${pageContext.request.contextPath}/downloadExcel",param);
	 
}  
var editIndex = undefined;
function endEditing(){
	if (editIndex == undefined){return true;}
	if ($('#dgs').datagrid('validateRow', editIndex)){
		var ed = $('#dgs').datagrid('getEditor', {index:editIndex,field:'reportCategory'});
		var name = $(ed.target).combobox('getText');
		$('#dgs').datagrid('getRows')[editIndex]['value'] = name;
		$('#dgs').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}
</script>
</head>


<body >
<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<input editable="false" style="display:none" name="deptflag" value="yes">
		<form id="serachFrom" method="post" style="margin-bottom: 0">
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">
		    		<tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>报送号:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportId" name="reportId" class="easyui-textbox" data-options="prompt: '报送号'"  style="width:100%;">
		    			</td>
			    		<td style="width:10%;text-align: right;" >
		    				<label>报送年度:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<!-- <input id="year" name="year" class="easyui-textbox" data-options="prompt: '报送年度'"  style="width:100%;"> -->
		    				<input style="width: 100%;" id="year" name="year" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
		    			</td>
		    			 <td style="width:8%;text-align: right;" >
		    				<label>报送季度:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="quarter" name="quarter" class="easyui-combobox"  style="width:100%;"
		    				 data-options="
					        prompt: '报送季度',editable:false,missingMessage:'请添加季度',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
						    method: 'get', valueField:'value',textField:'text', groupField:'group'">
		    			</td>
			    		<td style="width:8%;text-align: right;" >
		    				<label>报送类型:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportType" name="reportType" class="easyui-combobox"  style="width:100%;"
		    				data-options="prompt: '报表类别',editable:false, missingMessage:'请添加报表类别',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=reporttype',
						    method: 'get',valueField:'value',textField:'text', groupField:'group'">
		    			</td>
				    </tr>
				    <tr>
				    <!--  
				     <td style="width:8%;text-align: right;" >
		    				<label>报送状态:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportState" name="reportState" class="easyui-combobox"  style="width:100%;"
		    				data-options=" prompt: '报送状态',editable:false,
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=cfreportstate',
						    method: 'get',valueField:'value', textField:'text',groupField:'group'">
		    			</td>
		    			
			    		<td style="width:10%;text-align: right;" >
		    				<label>是否完成手工导入:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<select id="handleFlag" name="handleFlag" class="easyui-combobox" data-options="prompt: '是否完成手工导入',editable:false,"   style="width:100%;">
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
	<!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog"
		style="width: 300px; height:100px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,onOpen:onOpen,onClose:onClose">
		<div id="msg"></div>
    </div>
 <div class="easyui-panel" style="border: 0;width: 100%">
		<table id="dgs" class="easyui-datagrid"
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="false"
			autoRowHeight="false"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"	
			loadMsg="加载数据中..." 
			>
			<div id="toolbar">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="modedownload('1')" plain="true" data-options="iconCls:'icon-print'" style="width:140px;">分支机构模板下载</a>
	    		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="modedownload('1-data')" plain="true" data-options="iconCls:'icon-print'" style="width:150px;">分支机构数据下载</a>
	    		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="makerUpload()" plain="true"  data-options="iconCls:'icon-print'" style="width:80px;">上传文件</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="23%" align="center" halign="center" >报送号</th>
					<th field="year" width="12%" align="center" halign="center" >报送年度</th>
					<th field="quarter" width="24%" align="center" halign="center" hidden>报送季度</th>
					<th field="quarterName" width="12%" align="center" halign="center" >报送季度</th>
					<th field="reportType" width="24%" align="center" halign="center" hidden>报送类型</th>
					<th field="reportTypeName" width="12%" align="center" halign="center" >报送类型</th>
					<th field="reportCateGory" width="24%" align="center" halign="center" hidden>报表类型</th>
					<th field="reportCateGoryName" width="12%" align="center" halign="center" >报表类型</th>
					<!-- 
					<th field="reportState" width="10%" align="center" halign="center" hidden>报送状态</th>
					<th field="reportStateName" width="10%" align="center" halign="center" >报送状态</th>
					 -->
					<th field="comCode" width="10%" align="center" halign="center" >公司编码</th>
					<th field="comName" width="16%" align="center" halign="center">公司名称</th>
				</tr>
			</thead>
		</table>
	</div>
	
   <div id="dlg2" class="easyui-dialog" style="width:850px;height:450px;"
	    		top=20px buttons="#dlg-buttons" data-options="resizable:true,iconCls:'icon-more',modal:true,closed:true" title="反馈">
    	<table id="dg" class="easyui-datagrid"
			rownumbers="true"
			fitColumns="true"
			singleSelect="true"
			pagination="false" 
			striped="true"
			title="错误结果信息"
			nowrap="true"
			autoRowHeight="false"
			loadMsg="加载数据中..."
			>
			<thead>
				<tr>
					<th field="reportcode"  align="center">因子代码</th>
					<th field="reportname"  align="center">因子名称</th>
					<th field="fileName"  align="center">所属表格名称</th>
					<th field="remark" align="left">错误原因</th>
					<th field="reportCateGory" width="25" hidden>报送类型</th>
					
			</thead>
		</table>
    </div>
    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton showbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg2').dialog('close')">关闭</a>
    	<a href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-print'" onclick="exportXls('dg','错误结果信息')">导出</a>
    </div>
</body>
<script  type="text/javascript">
//ok
function ok(){	
	//判断报表名称是否与公司类型相符合
	if(!checkRepoType()){
		return false;
	}
	if(checkfiletype()){		
	$('#uploadForm').form('submit',{
		url: '${pageContext.request.contextPath}/riskdataupload/upload.do',
 		onSubmit: function(){
 			type = 0;
 			return checkinput();
 		},
		 success: function(result){
			$('#dlg').dialog('close');
			var result = eval('('+result+')');
			if (result.errorMsg){
				jsutil.msg.err('上传失败'+result.errorMsg)
			} else if(result.data!=null){
				$.messager.alert("警告","上传失败，数据格式异常信息如下!","warning",function(){
					$("#dg").datagrid({data:result.data});
					$("#dlg2").dialog('open');
				});
				//console.log(result.data);
			}else if(!result.success){
				$.messager.alert("提示","上传成功，但是数据精度有问题！","warning",function(){
					$("#dg").datagrid({data:result});
					$("#dlg2").dialog('open');
				});
			}else {
				jsutil.msg.alert('上传成功');
			}
		} 
	});
	}else{
		jsutil.msg.warn('上传文件未选择或上传文件类型错误！')
	}
}

function checkinput(){	
	if(type == 0){
		if ($('#uploadForm').form('validate')) {
			$('#msg').text('正在上传，请稍后.');
			$('#dlg').dialog('open').dialog('setTitle', '文件上传');
			return true;
		} else {
			return false;
		}
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

//报送类型和公司类型--分公司不能上传总公司的数据
function checkRepoType(){
	//报送类型 4标示 10号文总公司 5标示10号文分公司
	var reportType = $('#reportname').combobox('getValue');
	//公司类型 
	var comType = $('#companycode').combobox('getText');
	if(reportType==5){//10号文分公司
		if(comType.indexOf("分公司")==-1)
		{
			alert("报表名称和机构名称不匹配,请核查！");
			return false;
		}else{
			return true;
		}
		
	}	
	if(reportType==4){//10号文总公司
		if(comType.indexOf("分公司")==-1)
		{
			return true;	
		}else{
			alert("报表名称和机构名称不匹配,请核查！");
			return false;
		}
		
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

function modedownload(param){	
	//请求中添加时间戳参数，避免数据下载缓存问题
	var currDate = Date.parse(new Date());
	var rows = $('#dgs').datagrid('getSelections');
	if(rows.length==0||rows.length>1)
	{$.messager.alert('消息提示','请选择一条报送号进行数据下载!','error');return ;}

	endEditing();
	//传递的参数
	var pars = param;
	var deptflag = 'yes';
	type=1;
	$('#dlg').dialog('open').dialog('setTitle','正在下载');
	$('#msg').text('正在下载数据，请稍后.');
	var data =[{name:'reportId',value:rows[0].reportId},{name:'param',value:pars},{name:'currDate',value:currDate},{name:'deptflag',value:deptflag}];
	$.post('${pageContext.request.contextPath}/riskdataupload/modedownload.do',data,function(result){
		if (result.success){
			$('#dlg').dialog('close');		// close the dialog
			$('#dgs').datagrid('reload');	// reload the user data
			endEditing();
			location.href = '${pageContext.request.contextPath}/downLoadReport/downLoad.do?filepath='+result.data;	
		} else {
			$('#dlg').dialog('close');		// close the dialog
			$('#dgs').datagrid('reload');	// reload the user data
			endEditing();
			jsutil.msg.alert(result.errorMsg);
		}
	},'json');

}
//重置
function reset(){
	$('#serachFrom').form("clear");
	
}



function makerUpload(chunk){
	var rows = $('#dgs').datagrid('getSelections');	
	if(rows.length==0||rows.length>1){
		$.messager.alert('消息提示','请选择一条报送号进行文件上传!','error');
		return ;
	}
	
	endEditing();
	var year = rows[0].year;
	var quarter = rows[0].quarter;
	var reportId = rows[0].reportId;
	var reportname=rows[0].reportCateGory;
	var reporttype=rows[0].reportTypeName;
	var comCode=rows[0].comCode;
	
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
	var deptflag = 'yes';
	var _data = 'year=' + year + '&quarter=' + quarter +'&reportId=' + reportId+'&reportname='+reportname+'&reporttype='+reporttype+'&comCode='+comCode+'&deptflag='+deptflag;
	Uploader(chunk,_data,year,quarter,reportId,reportname,reporttype,function(files){
		if(files && files.length>0){
			 $("#res").text("成功上传："+files.join(",")); 
		}
	});
}

function Uploader(chunk,_data, year,quarter ,reportId,reportname,reporttype, callBack){
	var addWin = $('<div style="overflow: hidden;top:20px;"/>');
	var upladoer = $('<iframe/>');

	upladoer.attr({'src':'<%=basePath%>page/riskInfo/multiupload.jsp?chunk2='+ chunk + '&' + _data,width:'100%',height:'100%',frameborder:'0',scrolling:'no'});
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

/**
 * 根据iframe对象获取iframe的window对象
 * @param frame
 * @returns {Boolean}
 */
function GetFrameWindow(frame){
	return frame && typeof(frame)=='object' && frame.tagName == 'IFRAME' && frame.contentWindow;
}
</script>
</html>