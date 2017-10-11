<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String path=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); 
%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/color.css' />">
<script type="text/javascript" src="${contextPath}/js/yearvalidate/yearvalidate111.js "></script>
<title>规则管理</title>

</head>
<body>		
	<!-- 搜索模块 -->
	<div style="border:1px solid #D8D8D8;padding: 10px 0px 10px 0px;">
		<form id="serachFrom" method="post">
			<table cellpadding="5" border='0' style="width: 100%;">
	    		<tr>
	    			<td style="width:90px;text-align: right;padding-right: 20px;" >
	    				<label>条目序号:</label>
	    			</td>
	    			<td >
	    				<input id="itemNum" name="itemNum" class="easyui-textbox" data-options="prompt: '条目序号'">
	    			</td>
	    			<td style="width:90px;text-align: right;padding-right: 20px;" >
	    				<label>评估项目:</label>
	    			</td>
	    			<td >
	    				<input id="projectCode" name="projectCode" class="easyui-combobox" data-options="prompt: '评估项目'">
	    			</td>		
	    			<td colspan="2" style="text-align: right;padding-right: 20px">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">查询</a>
	    			</td>
	    		</tr>
	    	</table>
		</form>
	</div>
	<!-- 	操作，展示部分 -->
<div class="easyui-panel" style="border: 0;width: 100%">
		<table id="dg" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/alm_itemdefine/data.do?
			title="规则管理" 
			toolbar="#toolbar" 
			pagination="true" 
			striped="true"
			nowrap="false"
			singleSelect="true"
			autoRowHeight="false"
			style="width:auto;height:420px;"
			loadMsg="加载数据中..."
			method="get"
			>
			<thead style="height:50px;">
				<tr>
					<th field="itemNum">条目序号</th>
					<th field="projectCode" width="130px">评估项目</th>
					<th field="itemName" id="itemNameTh" width="450px">评估标准</th>
					<th field="standardScore"align="center">标准分值小计</th>
					<th field="sysIntegrityScore"align="center">制度健全性分值</th>
					<th field="folEfficiencyScore"align="center">遵循有效性分值</th>
					<!-- <th field="deptWeight">部门权重</th> -->
					<th field="temp" width="300px">评估备注</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editManage()">编辑</a>	
	    </div>
    </div>
    
    <!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:false,maximizable:true,closable:true"  buttons="#dlg-buttons">
	   	<form id="fm" method="post">
	   		<table cellpadding="5">
	   			<input type="hidden" name="itemCode">
	   			<tr>
	   				<td>所属部门:</td>
	   				<td><input class="easyui-combobox" 
							   name="deptNo" style="width: 380px;"						
			                   data-options="
					                 url:'${pageContext.request.contextPath}/codeSelect.do?type=deptNo',
					                 method:'get',
					                 valueField:'value',
					                 textField:'text',
					                 multiple:'true',					            
					                 panelHeight:'auto',
					                 editable : false
			         "></td>
			    </tr>
			    <tr>
	   				<td>部门权重:</td>
	   				<td><textarea name="deptWeight" class="easyui-validatebox" style="width: 380px; height: 25px;"></textarea></td>
	   			</tr>
	   			<tr>
	   				<td>评估备注:</td>
	   				<td colspan="3"><textarea name="temp" class="easyui-validatebox" style="width: 380px; height: 100px;"></textarea></td>
	   			</tr>
	   		</table>
	   	</form>
	    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveManage()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div>	
	</div>
    
</body>
	<script type="text/javascript">
	
	$(function(){
		 $('#projectCode').combobox({
				editable:false,
				url:'${pageContext.request.contextPath}/codeSelect.do?type=projectCode2',
				valueField:'value',
				textField:'text',
				method:'get'
			});
		 //alert(0); 
		 doCellTip();		 
		 //alert(1);
		 cancelCellTip();
		 //alert(2);
	});

	function doCellTip(){ 
	$('#dg').datagrid('doCellTip',{'max-width':'100px'}); 
	} 
	function cancelCellTip(){ 
	$('#dg').datagrid('cancelCellTip'); 
	}
	//编辑现有的制度管理
    function editManage(){
	    var rows = $('#dg').datagrid('getSelections');
	    if(rows.length==0){$.messager.alert('消息提示','请选择要编辑的制度信息!','error');return ;}
	    if(rows.length>1){$.messager.alert('消息提示','请选择一条制度进行编辑!','error');return ;}
	    var row = $('#dg').datagrid('getSelected');
	    $('#dlg').dialog('open').dialog('setTitle','编辑制度管理');
		$('#fm').form('load',row);
		url='${pageContext.request.contextPath}/alm_itemdefine/editManage.do?';
	}
	//保存编辑好的信息
    function saveManage(){
		$('#fm').form('submit',{
			url: url,
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				var result = eval('('+result+')');
				if (result.errorMsg){
					$.messager.show({
						title: '提示',
						msg: result.errorMsg
					});
				} else {
					$('#dlg').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
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
		//console.log(params);
    	
    	$('#dg').datagrid("load", params);
    	//alert(1);
    	$('#dg').datagrid('cancelCellTip');
    	//使用参数执行一次查询
    	$("#dg").datagrid({
    		onLoadSuccess: function(data){
    			//alert(2);
    			 $('#dg').datagrid('doCellTip',{cls:{'background-color':'#FFFFFF','color':'#19070B'},
    					delay:100,field:[$("#itemNameTh").attr('field')]});
    			// alert(3);
    		}
    	}); 
	}
	</script>
</html>