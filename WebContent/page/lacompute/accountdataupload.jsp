<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>完整数据上传</title>
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
	<div class="easyui-panel"  style="padding: 10px;width: 100%" data-options="footer:'#ft'">
		<form id="serachFrom" method="post">
			<table style="width: 100%;">
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>报送号:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportId" name="reportId" class="easyui-textbox" data-options="prompt: '报送号'"  style="width:60%;">
		    			</td>
			    		<td style="width:10%;" >
		    				<label>报送年度:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="year" name="year" class="easyui-textbox" data-options="prompt: '报送年度'"  style="width:60%;">
		    			</td>
		    			<td style="width:20%;" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">查询</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
	    				</td>
				    </tr>
				    </table>
		   <div class="easyui-panel" id="searchmore" style="height:auto; width:inherit;border:0;margin:0" data-options="collapsed:true">
				<table style="width: 100%;" >
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>报送季度:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="quarter" name="quarter" class="easyui-combobox" editable="false" style="width:60%;"
		    				 data-options="
					        prompt: '报送季度',
					        missingMessage:'请添加季度',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group'
					    	">
		    			</td>
			    		<td style="width:10%;" >
		    				<label>报送类型:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportType" name="reportType" class="easyui-combobox" editable="false" style="width:60%;"
		    				data-options="
					        prompt: '报表类别',
					        missingMessage:'请添加报表类别',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=reporttype',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group'">
		    			</td>		    			
		    			<td style="width:20%;">
		    			</td>
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>报送状态:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportState" name="reportState" class="easyui-combobox"  style="width:60%;"
		    				data-options="
					        prompt: '报送状态',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=cfreportstate',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group'">
		    			</td>
			    		<td style="width:10%;" >
		    				<label>是否完成手工导入:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<select id="handleFlag" name="handleFlag" class="easyui-combobox" data-options="prompt: '是否完成手工导入'"  editable="false" style="width:60%;">
		    					<option value=""></option>
		    					<option value="0">否</option>
		    					<option value="1">是</option>
		    				</select>
		    			</td>
		    			<td style="width:20%;">
		    			</td>
				    </tr>		    		
	    	</table>
	    	</div>
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
			url:'${pageContext.request.contextPath}/calCfReportInfo/listpage.do',
			iconCls:'icon-edit',
			toolbar:'#toolbar' ,
			rownumbers:'true',
			fitColumns:'true',
			singleSelect:'true', 
			pagination:'true', 
			striped:'true',
			nowrap:'true',
			onClickRow:onClickRow,
			autoRowHeight:'false',
			style:'width:100%;height:auto;',
			loadMsg:'加载数据中...'
			"
			>
			<div id="toolbar">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="modeldownloaddate()"  plain="true" data-options="iconCls:'icon-print'" >数据下载</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="25" >报送号</th>
					<th field="year" width="25" >报送年度</th>
					<th field="quarter" width="25" hidden>报送季度</th>
					<th field="quarterName" width="25" >报送季度</th>
					<th field="reportType" width="25" hidden>报送类型</th>
					<th field="reportTypeName" width="25" >报送类型</th>
					<th field="reportCategory" width="30" 
						formatter="resultCategory"
						editor="{type:'combobox',options:{
								valueField:'value',
								textField:'text',
								method:'get',
								url:'${pageContext.request.contextPath}/codeSelect/hasParam.do?type=reportname&params=1',
							}}">报送类别（数据下载时选择）</th>
					<th field="reportState" width="25" hidden>报送状态</th>
					<th field="reportStateName" width="25" >报送状态</th>
					<th field="handleFlag" width="25" formatter="isYON">是否完成手工导入</th>
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
</body>
<script  type="text/javascript">
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
{ type=0;
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
			location.href = result.data;			
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
	$('#dlg').dialog('open').dialog('setTitle','数据下载');
	$('#msg').text('正在下载数据，请稍后.');
	console.log(rows[0].reportId);
	var data =[{name:'reportId',value:rows[0].reportId },{name:'yearmonth',value:rows[0].year},{name:'quarter',value:rows[0].quarter},{name:'reporttype',value:rows[0].reportType },{name:'reportname',value:rows[0].reportCategory }];
	$.post('${pageContext.request.contextPath}/RiskExportDate/modeldownloaddate.do',data,function(result){
		if (result.success){
		$('#dlg').dialog('close')
			jsutil.msg.alert('下载成功')
			location.href = result.data;			
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
	$('#dg').datagrid("load", params);
	endEditing();
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
</script>
</html></html>