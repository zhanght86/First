<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/commons/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合同管理</title>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
<%@include file="/commons/statics.jsp"%>
	<h2>Client Side Pagination in DataGrid</h2>
	<p>This sample shows how to implement client side pagination in DataGrid.</p>
	<form>
		<table cellpadding="5">
				<tr>
	    			<td>合同名称:</td>
	    			<td>
	    			<input class="easyui-combobox" name="contractName" style="width:90%"
					data-options="
						url: '<%=path%>/codeSelect.do?type=contractName',
						method: 'get',
						valueField:'value',
						textField:'text',
						groupField:'group'
					"></td>
	    		</tr>
				<tr>
	    			<td>关联单号:</td>
	    			<td><input class="easyui-textbox" type="text" name="applyNo" data-options="required:true,validType:'length[1,5]',invalidMessage: '5.'"></input></td>
	    		</tr>
	    </table>
	</form>
	<div style="margin:20px 0;"></div>
	    <table id="dg" title="合同管理" class="easyui-datagrid" style="width:550px;height:250px"
    		url=${pageContext.request.contextPath}/contract/list.do
    		toolbar="#toolbar"
    		rownumbers="true" fitColumns="true" singleSelect="true" pagination="true">
    	<thead>
    		<tr>
    			<th field="ID" width="0">id</th>
    			<th field="CONTRACTID" width="50">合同号</th>
    			<th field="CONTRACTNAME" width="50">合同名称</th>
    			<th field="APPLYNO" width="50">关联单号</th>
    			<th field="REMARK" width="50">备注</th>
    		</tr>
    	</thead>
    </table>
    <div id="toolbar">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">新建合同</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑合同</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyUser()">删除合同</a>
    </div>
	    <div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
    		closed="true" buttons="#dlg-buttons">
    	<div class="ftitle">新增合同</div>
    	<form id="fm" method="post">
    		<input type="hidden" name="id"/>
    		<div class="fitem">
    			<label>合同号:</label>
    			<input name="contractId" class="easyui-validatebox" required="true">
    		</div>
    		<div class="fitem">
    			<label>合同名称:</label>
    			<input name="contractName" class="easyui-validatebox" required="true">
    		</div>
    		<div class="fitem">
    			<label>关联单号:</label>
    			<input name="applyNo">
    		</div>
    		<div class="fitem">
    			<label>备注:</label>
    			<input name="remark" class="easyui-validatebox" >
    		</div>
    	</form>
    </div>
    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div>
    <script type="text/javascript">
        function newUser(){
        	$('#tb').textbox({
        	    buttonText:'Search',
        	    iconCls:'icon-man',
        	    iconAlign:'left'
        	})
    	$('#dlg').dialog('open').dialog('setTitle','新增合同');
    	$('#fm').form('clear');
    		url='${pageContext.request.contextPath}/contract/add.do';
    	}
        function editUser(){
        var row = $('#dg').datagrid('getSelected');
        if (row){
        	$('#dlg').dialog('open').dialog('setTitle','编辑合同');
        	$('#fm').form('load',row);
        	url = '${pageContext.request.contextPath}/contract/update.do?id='+row.id;
        	}
        }
        function destroyUser(){
        	var row = $('#dg').datagrid('getSelected');
        	if (row){
        		$.messager.confirm('删除','确认删除该合同信息吗?',function(r){
        			if (r){
        				$.post('${pageContext.request.contextPath}/contract/delete.do',{id:row.ID},function(result){
        					if (result.success){
        						$('#dg').datagrid('reload');	// reload the user data
        					} else {
        						$.messager.show({	// show error message
        							title: 'Error',
        							msg: result.errorMsg
        						});
        					}
        				},'json');
        			}
        		});
        	}
        }
        function saveUser(){
        	$('#fm').form('submit',{
        		url: url,
        		onSubmit: function(){
        			return $(this).form('validate');
        		},
        		success: function(result){
        			var result = eval('('+result+')');
        			if (result.errorMsg){
        				$.messager.show({
        					title: 'Error',
        					msg: result.errorMsg
        				});
        			} else {
        				$('#dlg').dialog('close');		// close the dialog
        				$('#dg').datagrid('reload');	// reload the user data
        			}
        		}
        	});
        }
      </script>
</body>
</html>