<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>财务数据上传</title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@include file="/commons/statics.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/color.css' />">
<script type="text/javascript" src="/js/validateyear.js"></script>
</head>
	<!-- 搜索模块 -->
	<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
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
				    </tr>
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
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    				<label>报送状态:</label>
		    			</td>
		    			<td style="width:30%;">
		    				<input id="reportState" name="reportState" class="easyui-combobox"  style="width:60%;"
		    				data-options="
					        prompt: '报送状态',
						    url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=cfreportstate&params=4,4.5',
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
				    </tr>
		    		<tr height="30">
					    <td style="width:10%;" >
		    			</td>
		    			<td style="width:30%;">
		    			</td>
			    	<td style="width:10%;" >
		    				<span  style="width:10%;" ></span>
		    			</td>
		    			<td style="width:30%;">
		    				<span  style="width:10%;" ></span>
		    			</td>
	    			<td style="width:40%;" >
	    				<a href="javascript:void(0)"  class="easyui-linkbutton"  data-options="iconCls:'icon-print'" 
	    				          style="width:80px;"  onclick="modedownload()" >模板下载</a>     		
	    			
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">搜索</a>
	    			</td>
			     	</tr>
	    	</table>
		</form>
	</div>
	
	<!-- 	操作，展示部分 -->
	<div>
		<table id="dg11" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/calCfReportInfo/listpagehasparam.do?type=import
			toolbar="#toolbar11" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar11">
	    		<a href="#" class="easyui-linkbutton" plain="true" onclick="makerUpload(false)" data-options="iconCls:'icon-arrows-up'"
							style="width: 80px;">文件上传</a>
<!--  				<a href="#" class="easyui-linkbutton"  data-options="iconCls:'icon-print'" 
	    				          style="width:80px;" plain="true" onclick="uploadSuccess()" >确认完成</a>		
-->	    				          		
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="28" >报送号</th>
					<th field="year" width="25" >报送年度</th>
					<th field="quarter" width="25" hidden>报送季度</th>
					<th field="quarterName" width="25" >报送季度</th>
					<th field="reportType" width="25" hidden>报送类型</th>
					<th field="reportTypeName" width="25" >报送类型</th>
					<th field="reportState" width="25" hidden>报送状态</th>
					<th field="reportStateName" width="40" >报送状态</th>
					<th field="handleFlag" width="25" >是否完成手工导入</th>
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
   <div id="dlg2" class="easyui-dialog" style="width:850px;height:450px;padding:20px;"
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
			style="width:90%;height:320px;"
			loadMsg="加载数据中..."
			>
			<thead>
				<tr>
					<th field="reportcode"  align="center">因子代码</th>
					<th field="reportname"  align="center">因子名称</th>
					<th field="fileName"  align="center">所属表格名称</th>
					<th field="remark" align="center">错误原因</th>
			</thead>
		</table>
    </div>
    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton showbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg2').dialog('close')">关闭</a>
    	<a href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-print'" onclick="exportXls('dg','错误结果信息')">导出</a>
    </div>
    
    	<%-- <img alt="上传按钮图片" onclick="makerUpload(false)" style="width: 100px; height: 100px; margin-top: 25px;cursor: pointer;" src="${pageContext.request.contextPath }/images/upload.jpg"> --%>
    
</body>
<script  type="text/javascript">
function uploadSuccess(){
	var rows = $('#dg11').datagrid('getSelections');
	if(rows.length==0||rows.length>1){
		$.messager.alert('消息提示','请选择一条报送号进行操作!','error');
		return ;
	}
	
	if(rows[0].reportTypeName!="季度报告"){
		jsutil.msg.alert('只有季度报告才需要上传数据！');
		return false;
	}
	var data =[{name:'reportId',value:rows[0].reportId }];
	
	$.messager.confirm('撤销操作','财务接口数据全部上传完成吗?',function(r){
		if (r){
			$.post('${pageContext.request.contextPath}/abc_financeInterface/updateState.do',data,function(result){
				if (result.success){
					$('#dg11').datagrid('reload');	// reload the user data
					$.messager.alert("提示","上传完成","info");
					$('#dg11').datagrid('reload');
				} else {
					$.messager.alert("警告","上传失败","warning");
				}
			},'json');
		}
	});
}

function Uploader(chunk, data, year,quarter ,reportId, callBack){
	var addWin = $('<div style="overflow: hidden;top:20px;"/>');
	var upladoer = $('<iframe/>');

	upladoer.attr({'src':'<%=basePath%>page/abc/abc_financeMultiupload.jsp?chunk2='+ chunk + '&' + data,width:'100%',height:'100%',frameborder:'0',scrolling:'no'});
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
 
function makerUpload(chunk){
	var rows = $('#dg11').datagrid('getSelections');
	if(rows.length==0||rows.length>1){
		$.messager.alert('消息提示','请选择一条报送号进行文件上传!','error');
		return ;
	}
	
	var quarter = rows[0].quarter;
	var reportId = rows[0].reportId;
	var year = rows[0].year;

	var _data = 'year=' + year + '&quarter=' + quarter +'&reportId=' + reportId ;
 	
	Uploader(chunk,_data,year,quarter,reportId,function(files){
		$('#dg11').datagrid('reload');
		if(files && files.length>0){
			/* $("#res").text("成功上传："+files.join(",")); */
		}
	});
}


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
	$('#dg11').datagrid("load", params);
}

//ok
function upload2(){	
	if(checkfiletype()){		
	$('#uploadForm').form('submit',{
		url: '${pageContext.request.contextPath}/abc_financeInterface/finance.do',
 		onSubmit: function(){
 			return checkinput();
 		},
		 success: function(result){
			$('#dlg').dialog('close')
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
	if(file == "") {
		jsutil.msg.alert('文件不能为空，请选择一个文件！');
		return false;
	}
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
function modedownload(){
	$('#serachFrom').form('submit',{
		url: '${pageContext.request.contextPath}/abc_financeInterface/financeModeDownload.do',
 		onSubmit: function(){
// 			$('#msg3').text('正在下载，请稍后.');
// 			$('#dlg3').dialog('open').dialog('setTitle','模板下载');
 			return true;
 		},
		 success: function(result){
//			$('#dlg3').dialog('close')
			var result = eval('('+result+')');
			if (result.errorMsg){
				jsutil.msg.err('下载失败'+result.errorMsg)
			} else {
				jsutil.msg.alert('下载成功')
			}
		} 
	});
	
	
}
//重置
function reset(){
	$('#uploadForm').form("clear");
}
</script>
</html>