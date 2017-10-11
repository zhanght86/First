<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@taglib uri="http://www.sinosoft.com/token" prefix="k"%>
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
<div id="userlist2" >
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form id="serachFrom" method="post">
		<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">
	    		<tr>
	    			<td style="width:8%;text-align: right;" >
	    				<label>用户编码:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" id="userCodeS" name="userCodeS" class="easyui-textbox" data-options="prompt: '用户编码'">
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>用户名称:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" id="userName"  name="userName" class="easyui-textbox" data-options="prompt: '用户名称'">
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>所属机构:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" id="comName" name="comName" class="easyui-combobox" data-options="prompt: '所属机构',editable:false">
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>所属部门:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" id="deptName"  name="deptName" class="easyui-combobox" data-options="prompt: '所属部门',editable:false">
	    			</td>
	    		</tr>
	    		<tr>
	    			<td colspan="8" style="text-align: right; padding-right: 20px" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">查询</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:8%;">重置</a>
	    			</td>
	    		</tr>
	    	</table>
		<!-- <table cellpadding="5">
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
	    			<td><input class="easyui-textbox" type="text" name="applyNo"></input></td>
	    		</tr>
	    </table> -->
	</form>	
</div>
<div class="easyui-panel" style="border: 0;width: 100%;">
	    <table id="dg"class="easyui-datagrid" 
    		toolbar="#toolbar"
    		rownumbers="true" 
    		fitColumns="true" 
    		singleSelect="true" 
    		pagination="true"
    		striped="true" 
    		sortName="userCode" 
    		nowrap="false"
			autoRowHeight="false"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			loadMsg="加载数据中..."
			data-options="pageSize:10,pageList:[10,20,30,40,50]"
    		>
<!--     启动多列排序		multiSort="true" -->
    	<thead>
    		<tr>
    			<th halign="center" align="center" field="ck" checkbox=true></th>
    			<th halign="center" align="center" field="id" width="6%" hidden>id</th>
    			<th halign="center" align="center" field="userCode" width="10%"  sortable="true">用户编码</th>
    			<th halign="center" align="center" field="userName" width="10%"  sortable="true">用户名称</th>
    			<th halign="center" align="center" field="comCodeName" width="20%">所属机构</th>
    			<th halign="center" align="center" field="deptCodeName" width="20%" >所属部门</th>
    			<th halign="center" align="center" field="email" width="15%"  sortable="true">公司邮箱</th>
    			<th halign="center" align="center" field="idCardNo" width="15%"  sortable="true" hidden="true">身份证号</th>
    			<th halign="center" align="center" field="phone" width="13%"  sortable="true">手机号</th>
    			<th halign="center" align="center" field="useFlagName" width="8%"  sortable="true">使用状态</th>
    		</tr>
    	</thead>
    </table>
    </div>
    <div id="toolbar">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">新建用户</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑用户</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="userRole()">用户角色</a>
    </div>
	<!--  新增弹框界面-->
<div id="dlg" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons">
    	<form id="fm" method="post">
    		<table cellpadding="5">
	    		<tr>
	    			<td><label>用户编码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="userCode" name="userCode" data-options="prompt:'用户代码',required:true,readonly:true,missingMessage:'请添加用户代码'"><font color="red">*</font>
					</td>
					<td><label>用户名称:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="userName" name="userName" class="easyui-validatebox"data-options="prompt:'用户名称',required:true,missingMessage:'请添加用户名称'"><font color="red">*</font>
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>所属机构:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id ="comCode" name="comCode" class="easyui-combobox" style="width: 140px;" data-options="prompt:'所属公司',required:true,editable:false"><font color="red">*</font>
					</td>
					<td><label>所属部门:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id ="deptCode" name="deptCode" class="easyui-combobox" style="width: 140px;" data-options="prompt:'所属部门',required:true,editable:false"><font color="red">*</font>
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>手机号:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  name="phone" class="easyui-validatebox textbox" data-options="prompt:'手机号',required:true" /><font color="red">*</font>
					</td>
					<td><label>邮箱:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  name="email" class="easyui-validatebox textbox" data-options="prompt:'邮箱',required:true" /><font color="red">*</font>
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>操作机构:</label></td>
	    			<td><input id="branchs" name = "branchs" class="easyui-combobox" data-options="prompt: '操作机构',editable:false,required:true" ><font color="red">*</font></input></td>
	    			<td><label>操作部门:</label></td>
	    			<td><input id="depts" name = "depts" class="easyui-combobox" data-options="prompt: '操作部门',editable:false,required:true" ><font color="red">*</font></input></td>
	    		</tr>
	    		<tr>
	    			<td><label>备注:</label></td>
	    			<td><input class="easyui-textbox" type="text" name="remark" class="easyui-validatebox" data-options="prompt: '备注'"></td>
	    			<td><label>使用状态:</label></td>
	    			<td><input id="useFlag" name = "useFlag" class="easyui-combobox" data-options="prompt: '使用状态',editable:false,required:true" ><font color="red">*</font></input></td>
	    		</tr>
	    	</table>
    		<input name="id" type="hidden" >
    		<input type='Hidden' name="springmvc.token" value="">
    		<%-- <k:token/> --%>
    	</form>
    
    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="cancel()">取消</a>
    </div>
  </div>
<!--   用户角色配置 -->
  <div id="roleGrid" class="easyui-dialog" style="width: 40%;top: 25%;padding:10px 10px 10px 10px;" data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" buttons="#roleData-buttons">
	<div class="easyui-panel" style="width: 100%;">
		<ul id="roleTree" class="easyui-tree"  data-options="method:'get',animate:true,checkbox:true"></ul>
	</div>
	<div id="roleData-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUserRole()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#roleGrid').dialog('close')">取消</a>
    </div>
  </div>
  
</div>
</body>
<script type="text/javascript">
$(function(){
	serach();
	$('#deptName').combobox({
    	editable:false,
    	url:'${pageContext.request.contextPath}/codeSelect.do?type=deptNoAll',
        valueField:'value',
		textField:'text',
		method: 'get',
    });
	$('#comName').combobox({
		  valueField:'value',
		  textField:'text',
		  url:'${pageContext.request.contextPath}/codeSelect.do?type=companyCode',
		  method:'GET'
		}); 
	$('#comCode').combobox({
		  valueField:'value',
		  textField:'text',
		  url:'${pageContext.request.contextPath}/codeSelect.do?type=companyCode',
		  method:'GET',
		  onHidePanel: function(){
		      $("#deptCode").combobox("setValue",'');
		      var comCode = $('#comCode').combobox('getValue');		
		       $.ajax({
		        type: "GET",
		        url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=deptNo&params='+comCode,
		        cache: false,
		        dataType : "json",
		        success: function(data){
		            $("#deptCode").combobox("loadData",data);
		        }
		      });  
		      $.ajax({
			        type: "GET",
			        url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=companycode&params='+comCode,
			        cache: false,
			        dataType : "json",
			        success: function(data){
			            $("#branchs").combobox("loadData",data);
			        }
			      }); 
		   }
	}); 
	$('#deptCode').combobox({ 
		  valueField:'value',
		  textField:'text',
	});
	$('#useFlag').combobox({
    	editable:false,
    	url:'${pageContext.request.contextPath}/codeSelect.do?type=UseFlag',
        valueField:'value',
		textField:'text',
		method: 'get',
    });
	$('#branchs').combobox({    
			valueField:'value',
        	textField:'text',
		  multiple: true,
		  required:true,
		  checkbox:true,
		  method:'GET',
		  formatter: function (row) {
              var opts = $(this).combobox('options');
              return row[opts.textField];
          },
          onLoadSuccess: function () {
              var opts = $(this).combobox('options');
              var target = this;
              var values = $(target).combobox('getValues');
              $.map(values, function (value) {
                  var el = opts.finder.getEl(target, value);
                  el.find('input.combobox-checkbox')._propAttr('checked', true);
              })
          },
          onSelect: function (row) {
              var opts = $(this).combobox('options');
              var el = opts.finder.getEl(this, row[opts.valueField]);
              el.find('input.combobox-checkbox')._propAttr('checked', true);
          },
          onUnselect: function (row) {
              var opts = $(this).combobox('options');
              var el = opts.finder.getEl(this, row[opts.valueField]);
              el.find('input.combobox-checkbox')._propAttr('checked', false);
          }
          
	}); 
	$('#depts').combobox({    
		valueField:'value',
    	textField:'text',
	  multiple: true,
	  url:'${pageContext.request.contextPath}/codeSelect.do?type=deptNoAll',
	  required:true,
	  checkbox:true,
	  method:'GET',
	  formatter: function (row) {
          var opts = $(this).combobox('options');
          return row[opts.textField];
      },
      onLoadSuccess: function () {
          var opts = $(this).combobox('options');
          var target = this;
          var values = $(target).combobox('getValues');
          $.map(values, function (value) {
              var el = opts.finder.getEl(target, value);
              el.find('input.combobox-checkbox')._propAttr('checked', true);
          })
      },
      onSelect: function (row) {
          var opts = $(this).combobox('options');
          var el = opts.finder.getEl(this, row[opts.valueField]);
          el.find('input.combobox-checkbox')._propAttr('checked', true);
      },
      onUnselect: function (row) {
          var opts = $(this).combobox('options');
          var el = opts.finder.getEl(this, row[opts.valueField]);
          el.find('input.combobox-checkbox')._propAttr('checked', false);
      }
      
});
	
});
//重置
function reset(){
	$('#serachFrom').form("clear");
}

function cancel(){
	$('#dlg').dialog('close');
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
	//$('#dg').datagrid("load", params);
    $("#dg").datagrid({
	    url:'${pageContext.request.contextPath}/userinfo/list.do',
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
    
//     新建用户
	function newUser(){
		 $('#userCode').textbox('readonly',false);
		$('#dlg').dialog('open').dialog('setTitle','新增用户');
		//var token=$('#fm').find('input[name="springmvc.token"]').val();
		$('#fm').form('clear');
		//$('#fm').find('input[name="springmvc.token"]').val(token);
		jsutil.tool.refreshToken('${pageContext.request.contextPath}',$('#fm').find('input[name="springmvc.token"]'));
			url='${pageContext.request.contextPath}/userinfo/add.do';
	}
    
	
//         编译用户
    function editUser(){
    	$('#userCode').textbox('readonly',true);
        var rows = $('#dg').datagrid('getSelections');
        if(rows.length==0){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
        var row = $('#dg').datagrid('getSelected');
        console.log(row);
        if (row){
        	$('#dlg').dialog('open').dialog('setTitle','修改用户');
        	$('#fm').form('load',row);
        	url = '${pageContext.request.contextPath}/userinfo/update.do?id='+row.id;
        }
        
    }

//         用户保存
      function saveUser(){
		//var brachs = $('#branchs').combobox('getValues');
		
     	$('#fm').form('submit',{
     		url: url,
     		onSubmit: function(){
     			return $(this).form('validate');
     		},
     		success: function(result){
     			eval("result="+result);
     			if(result.success){
     				$('#dlg').dialog('close');		// close the dialog
     				$('#dg').datagrid('reload');	// reload the user data
     				jsutil.msg.alert("保存成功");
     			}else{
     				jsutil.msg.alert(result.errorMsg);
     				jsutil.tool.refreshToken('${pageContext.request.contextPath}',$('#fm').find('input[name="springmvc.token"]'));
     			}
     		},
     		fail:function(){
     			jsutil.msg.alert("非法操作");
     			jsutil.tool.refreshToken('${pageContext.request.contextPath}',$('#fm').find('input[name="springmvc.token"]'));
     		}
     	});
      }
// 		用户角色配置
		function userRole(){
			var rows = $('#dg').datagrid('getSelections');
		    if(rows.length==0){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
		    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
		    $('#roleGrid').dialog('open').dialog('setTitle','用户角色配置');
		    $('#roleTree').tree({
		        url:'${pageContext.request.contextPath}/role/json.do?userId='+rows[0].id
		    });
		}
		
// 		用户机构配置
		function userBranch(){
		    $('#branchGrid').dialog('open').dialog('setTitle','用户机构配置');
		    $('#branchTree').tree({
		    	url:'${pageContext.request.contextPath}/codeSelect.do?type=companyCode'
		    });
		}
		
// 		用户角色保存
		function saveUserRole(){
			var row = $('#dg').datagrid('getSelected');
			var nodes = $('#roleTree').tree('getChecked');//获取选中角色信息
			var idArr=[],ids=[];
			for(var obj in nodes){
				idArr.push(nodes[obj].id);
			}
			var ids =[{name:'roleIDs',value:idArr.join(',') },{name:'userID',value:row.id }];
			$.post('${pageContext.request.contextPath}/userinfo/UMmap.do',ids,function(result){
				if (result.success){
					$('#roleGrid').dialog('close');		// close the dialog
					jsutil.msg.alert("保存成功");
					/* $.messager.show({	// show error message
						title: 'info',
						msg: '保存成功'
					}); */
				} else {
					$.messager.show({	// show error message
						title: 'Error',
						msg: result.errorMsg
					});
				}
			},'json');
		}
		
		function exportXls(gridId, xlsName) {
			var param=jsutil.core.exportXls(gridId,xlsName);
			jsutil.core.download("${pageContext.request.contextPath}/downloadExcel",param);
		}
     </script>
</html>