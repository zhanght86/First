<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String path=request.getContextPath(); 
%>
<title>风险评级xbrl生成</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
</head>
<body>
<%@include file="/commons/statics.jsp"%>
<script type="text/javascript" src="<c:url value='/js/validateyear.js' />"></script>
	<!-- 搜索模块 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form id="serachFrom" method="post" style="margin-bottom: 0">
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">
		    		<tr>
					    <td style="width:8%;text-align: right;" >
		    				<label>报送号:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportId" name="reportId" class="easyui-textbox" data-options="prompt: '报送号'"  style="width:100%;">
		    			</td>
			    		<td style="width:8%;text-align: right;" >
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
		    				 data-options="prompt: '报送季度',editable:false,missingMessage:'请添加季度',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
						    method: 'get', valueField:'value',textField:'text',groupField:'group'">
		    			</td>
			    		<td style="width:8%;text-align: right;" >
		    				<label>报送类型:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportType" name="reportType" class="easyui-combobox"  style="width:100%;"
		    				data-options="prompt: '报表类别',editable:false, missingMessage:'请添加报表类别',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=reporttype',
						    method: 'get',valueField:'value',textField:'text',groupField:'group'">
		    			</td>
				    </tr>
				    <!--  
				    <tr>
				    	<td style="width:8%;text-align: right;" >
		    				<label>报送状态:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportState" name="reportState" class="easyui-combobox"  style="width:100%;"
		    				data-options="prompt: '报送状态',editable:false,
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=cfreportstate',
						    method: 'get', valueField:'value',textField:'text',groupField:'group'">
		    			</td>
				    </tr>
				    -->
				    <tr>
		    			<td colspan="8" style="text-align: right;padding-right: 20px" >
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
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="ok()" plain="true" data-options="iconCls:'icon-ok'" style="width:140px;">报送xbrl文件生成</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="filedownload()"  plain="true" data-options="iconCls:'icon-print'" style="width:140px;">报送xbrl文件下载</a>
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
		<input type="hidden" name="userCompany" id="userCompany" value="${currentUser.comCode}" />
	</div>
	<!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog"
		style="width: 300px; height:100px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,onOpen:onOpen,onClose:onClose">
		<div id="msg"></div>
    </div>
	<div id="dlghandle" class="easyui-dialog"
		style="width: 400px; height: 180px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlghandle-buttons">
		<form id="exportfm" method="post">
			<table cellpadding="5">
					<tr>
		    			<td><label>报送机构:</label></td>
		    			<td>
							 <select  id="companyTree" class="easyui-combotree" style="width:145px" required
	       		 				data-options="prompt:'报送机构',missingMessage:'请选择机构'">
	       					</select>
	       					<input type="hidden" name="company" id="company" />
						</td>
		    		</tr>
					<tr>
		    			<td><label>是否生成下级公司:</label></td>
		    			<td>
	       					<input type="radio" class="easyui-radio" name="isAllFlag" value="1">是
	       					<input type="radio" class="easyui-radio" name="isAllFlag" value="0" checked>否
						</td>
		    		</tr>
		    	</table>
		    	<input type="hidden" name="reportId" id="expoetReportId" />
		    </form>
		    <div id="dlghandle-buttons">
		    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="exportXbrl();">生成报告</a>
		    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlghandle').dialog('close')">取消</a>
	   		 </div>
    </div>
	<div id="dlghandledown" class="easyui-dialog"
		style="width: 400px; height: 180px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlghandledown-buttons">
		<form id="downloadFrom" method="post">
			<table cellpadding="5">
					<tr>
		    			<td><label>报送机构:</label></td>
		    			<td>
							 <select  id="companyTree1" class="easyui-combotree" style="width:145px" required
	       		 				data-options="prompt:'报送机构',missingMessage:'请选择机构'">
	       					</select>
	       					<input type="hidden" name="company" id="company1" />
						</td>
		    		</tr>
					<tr>
		    			<td><label>是否生成下级公司:</label></td>
		    			<td>
	       					<input type="radio" class="easyui-radio" name="isAllFlag" value="1">是
	       					<input type="radio" class="easyui-radio" name="isAllFlag" value="0" checked>否
						</td>
		    		</tr>
		    	</table>
		    	<input type="hidden" name="reportId" id="downReportId" />
		    </form>
		    <div id="dlghandledown-buttons">
		    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="downXbrl();">下载报告</a>
		    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlghandledown').dialog('close')">取消</a>
	   		 </div>
    </div>
</body>
<script type="text/javascript">
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

function isYON(value){
	if(value=='0'){
		return '否';
	}else{
		return '是';
		} 
}
function ok(){
	var rows = $('#dg').datagrid('getSelections');
	if(rows.length==0||rows.length>1){$.messager.alert('消息提示','请选择一条报送号进行生成!','error');return ;}
	$('#dlghandle').dialog('open').dialog('setTitle','生成xbrl操作选择');
	var handleCompany=$('#userCompany').val();
	$('#companyTree').combotree('reload');
	//$('#exportfm').form('clear');
	//$('#company').combotree('loadData', [{id: 1,text: 'Languages'}]);
	$.ajax({
		url:'${pageContext.request.contextPath}/branchinfo/listCompanyByUperCompany.do?company='+handleCompany,
		type:'get',
		success:function(result){
			$('#companyTree').combotree('loadData',result);
		}
	});
	$('#companyTree').combotree('setValue',handleCompany);
	$('#expoetReportId').val(rows[0].reportId);
}
function exportXbrl(){
	var t = $('#companyTree').combotree('tree');	// get the tree object
	var n = t.tree('getSelected');
	if(n){
		$('#company').val(n.id);
	}
	$('#dlg').dialog('open').dialog('setTitle','生成xbrl');
	$('#msg').text('正在导出xbrl文件，请稍后.');
	var data =[{name:'reportId',value:$('#expoetReportId').val() }];
	$('#exportfm').form('submit',{
		url: '${pageContext.request.contextPath}/reportcreate/reportcreate.do',
 		onSubmit: function(){
 			return $(this).form('validate');
 		},
		success: function(result){
			var result = eval('('+result+')');
			if (result.success){
				$('#dlg').dialog('close');		// close the dialog
				$('#dlghandle').dialog('close');
				$('#dg').datagrid('reload');	// reload the user data
				jsutil.msg.alert("报送文件生成成功!");
			} else {
				$('#dlg').dialog('close');		// close the dialog
				//$('#dg').datagrid('reload');	// reload the user data
				jsutil.msg.alert(result.errorMsg);
			}
		}
	}); 
}
function filedownload(){
	var rows = $('#dg').datagrid('getSelections');
	if(rows.length==0||rows.length>1){$.messager.alert('消息提示','请选择一条报送号进行下载!','error');return ;}
	if(rows[0].reportState!="6"){
		jsutil.msg.alert(rows[0].reportId+'的报送状态必须为已生成xbrl状态');
		return false;
	}
	$('#dlghandledown').dialog('open').dialog('setTitle','下载xbrl操作选择');
	var handleCompany=$('#userCompany').val();
	$('#companyTree1').combotree('reload');
	$.ajax({
		url:'${pageContext.request.contextPath}/branchinfo/listCompanyByUperCompany.do?company='+handleCompany,
		type:'get',
		success:function(result){
			$('#companyTree1').combotree('loadData',result);
		}
	});
	$('#companyTree1').combotree('setValue',handleCompany);
	$('#downReportId').val(rows[0].reportId);
}
function downXbrl(){
	var t = $('#companyTree1').combotree('tree');	// get the tree object
	var n = t.tree('getSelected');
	if(n){
		$('#company1').val(n.id);
	}
	//$('#dlg').dialog('open').dialog('setTitle','下载xbrl');
	//$('#msg').text('正在下载文件，请稍后.');
	$('#downloadFrom').form('submit',{
		url: '${pageContext.request.contextPath}/reportcreate/download.do',
 		onSubmit: function(){
 			return $(this).form('validate');
 		},
		success: function(result){
			var result = eval('('+result+')');
			if (result.errorMsg){
				jsutil.msg.alert('下载失败'+result.errorMsg);
			} else {
				jsutil.msg.alert('下载成功!');
			}
		}
	});
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
			text='正在进行xbrl生成操作，请稍后.';
		}else if(i==2){
			text='正在进行xbrl生成操作，请稍后..';
		}else if(i==3){
			text='正在进行xbrl生成操作，请稍后...';
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
	$("#dg").datagrid({
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
function updateState(){
	var rows = $('#dg').datagrid('getSelections');
	if(rows.length==0||rows.length>1){$.messager.alert('消息提示','请选择一条报送号进行操作!','error');return ;}
	if(rows[0].reportState!="5"&&rows[0].reportState!="6"){
		jsutil.msg.alert('只有校验完毕状态的报送单才可以进行重新校验！');
		return false;
	}
	var data =[{name:'reportId',value:rows[0].reportId }];
	$.post('${pageContext.request.contextPath}/dataCheck/updateState.do',data,function(result){
		if (result.success){
			$('#dg').datagrid('reload');	// reload the user data
			$.messager.alert("提示","状态改变成功","info");
		} else {
			$.messager.alert("警告","状态改变错误","warning");
		}
	},'json');
} 
</script>
</html>