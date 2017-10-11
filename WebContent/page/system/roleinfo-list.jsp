<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String path=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); 
%>
<title>角色新增</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
</head>
<body>
<%@include file="/commons/statics.jsp"%>
	<!-- 搜索模块 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form id="serachFrom" method="post">
		<table cellpadding="5" style="width: 100%; overflow: hidden;table-layout: fixed;" border="0" >
	    		<tr>
	    			<td style="width:8%;text-align: right;" >
	    				<label>角色代码:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" id="contractId" name="roleCode" class="easyui-textbox" data-options="prompt: '角色代码'"  style="width:60%;">
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>角色名称:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" id="contractName"  name="roleName" class="easyui-textbox" data-options="prompt: '角色名称'"  style="width:60%;">
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>备注:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" id="remark" name="remark" class="easyui-textbox" data-options="prompt: '备注'"  style="width:60%;">
	    			</td>
	    			<td style="width:25%;" colspan="2"></td>
	    		</tr>
	    		<tr >
    				<td colspan="8" style="text-align: right;padding-right: 20px;">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">查询</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:8%;">重置</a>
	    			</td>
	    		</tr>
	    	</table>
		</form>
	</div>
<!-- 	操作，展示部分 -->
	<div>
		<table id="dg" class="easyui-datagrid"
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="false"
			sortName="roleCode"
			autoRowHeight="false"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">新建角色</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑角色</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyUser()">删除角色</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="roleMenu()">角色菜单</a>
			</div>
			<thead>
				<tr>
					<th field="ck"   checkbox="true"></th>
					<th field="id" width="8%" align="center" halign="center">id</th>
					<th field="roleCode" width="25%" sortable="true" align="center" halign="center">角色代码</th>
					<th field="roleName" width="25%" sortable="true" align="center" halign="center">角色名称</th>
					<th field="remark" width="37%" sortable="true" align="center" halign="center">备注</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog"
		style="width: 400px; height: 280px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
	    		<tr>
	    			<td><label>角色代码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="roleCode" name="roleCode" class="easyui-validatebox"data-options="prompt:'角色代码',required:true,missingMessage:'请添加角色代码'"><font color="red">*</font>
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>角色名称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  name="roleName" class="easyui-validatebox textbox" data-options="prompt:'角色名称',required:true,missingMessage:'请添加角色名称'"></"><font color="red">*</font>
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>备注:</label></td>
	    			<td><input class="easyui-textbox" type="text" name="remark" class="easyui-validatebox" data-options="prompt: '备注'"></td>
	    		</tr>
	    	</table>
	    	<input type="hidden" name="id" value="000"/>
		</form>
		<div id="dlg-buttons">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	    </div>
    </div>
    <!-- 	角色菜单操作 -->
  <div id="menuGrid" class="easyui-dialog" style="width: 40%;top: 25%;padding:10px 10px 10px 10px;" data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" buttons="#roleData-buttons">
	<div class="easyui-panel" style="width: 100%;">
		<ul id="menuTree" class="easyui-tree"  data-options="method:'get',animate:true,checkbox:true"></ul>
	</div>
	<div id="roleData-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRoleMenu()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#menuGrid').dialog('close')">取消</a>
    </div>
  </div>
</body>
<script  type="text/javascript">
//复选框
function formatter4(val,row,index){
	 return "<input type ='checkbox' style='width:15px;height:18px;' name='ck' value='"+row.id+"' id='"+row.id+"' onclick='asss("+row.id+")'/>";
};
//设置复选框为单选
function asss(id){
	 $("input[type='checkbox']").prop("checked",false);
	 $("#"+id).prop("checked",true);
}
// 新建
    function newUser(){
    	$('#dlg').dialog('open').dialog('setTitle','新增角色');
    	$('#fm').form('clear');
    	url='${pageContext.request.contextPath}/role/add.do';
	}
//保存
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
				$('#dg').datagrid('reload');// reload the user data
				$.messager.show({
					title: '消息',
					msg: '保存成功',
					timeout:2000
				});
			}
		}
	});
}
//修改
function editUser(){
    var rows = $('#dg').datagrid('getSelections');
    if(!rows){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','编辑角色');
	url = '${pageContext.request.contextPath}/role/update.do?id='+rows[0].id;
	$('#fm').form('load',rows[0]);

}

//删除
function destroyUser(){
	var rows = $('#dg').datagrid('getSelections');
	var idArr = new Array();
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].id;
	}
	var data =[{name:'ids',value:idArr.join(',') }];
	console.log(rows);	
	if(rows.length==0){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
		$.messager.confirm('删除','确认删除该角色信息吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/role/delete.do',data,function(result){
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
//	重置
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
	console.log(params);
	//$('#dg').datagrid("load", params);
	$("#dg").datagrid({
		url:'${pageContext.request.contextPath}/role/list.do',
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
//角色菜单
function roleMenu(){
	var rows = $('#dg').datagrid('getSelections');
    if(rows.length==0){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
    $('#menuGrid').dialog('open').dialog('setTitle','用户角色配置');
    $('#menuTree').tree({
        url:'${pageContext.request.contextPath}/menuinfo/getCheckData.do?roleId='+rows[0].id
    });
	
}
function addNode(){
	var ids=[];
	var checkData = $('#menuTree').tree('getChecked');
	if(checkData.length==0){
		$.messager.alert('消息提示','请勾选菜单信息!','info');return ;
	}
	for(var index in checkData){
		ids.push(checkData[index].id);
		var parent = $('#menuTree').tree('getParent',checkData[index].target);
		if(parent==null) 
			continue;
		else
			ids.push(parent.id);
	}
	return ids;
}

function saveRoleToMenu(){
	if(ids.length<=0){return;}
	var row = $('#dg').datagrid('getSelected');
	var roleId=row.id;
	var data =[{name:'roleId',value:roleId },{name:'menuId',value:ids.join(',')}];
	console.log(data);
	$.post('${pageContext.request.contextPath}/role/roleToMenu.do',data,function(result){
		if (result.success){
			$('#roleGrid').dialog('close');		// close the dialog
			$.messager.show({	// show error message
				title: 'info',
				msg: '保存成功'
			});
		} else {
			$.messager.show({	// show error message
				title: 'Error',
				msg: result.errorMsg
			});
		}
	},'json');
}

function getNodeData(){
	$('#roleTree').tree({
		url:'${pageContext.request.contextPath}/menuinfo/list.do',
		onLoadSuccess:function(node,data){
			console.log(data)
		}
	});
}
//	用户角色保存
function saveRoleMenu(){
	var row = $('#dg').datagrid('getSelected');
	var nodes = $('#menuTree').tree('getChecked');//获取选中角色信息
	var idArr=[],ids=[];
// 	for(var obj in nodes){
// 		idArr.push(nodes[obj].id);
// 	}
	var ids = addNode()
	var idArr =[{name:'menuId',value:ids.join(',') },{name:'roleId',value:row.id }];
	$.post('${pageContext.request.contextPath}/role/roleToMenu.do',idArr,function(result){
		if (result.success){
			$('#menuGrid').dialog('close');		// close the dialog
			$.messager.show({	// show error message
				title: 'info',
				msg: '保存成功'
			});
		} else {
			$.messager.show({	// show error message
				title: 'Error',
				msg: result.errorMsg
			});
		}
	},'json');
}
$(function (){

	//自定义验证
	//条件：,required:true,novalidate:false(默认false)
	$.extend($.fn.validatebox.defaults.rules, {    
        roleCode: {    
            validator: function(value){    
                var flag = false;  
                console.log($(this).val().length);
                  return flag;  
            },  
           message: '您输入的用户名已存在，请更换。'    
        }
	})
	serach();
});

</script>
</html>