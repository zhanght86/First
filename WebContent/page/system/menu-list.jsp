<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/commons/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
	text-align:center;
}
.datagrid-header-row td div {
	text-align:center;
}
</style>
</head>
<body >
<%@include file="/commons/statics.jsp"%>
	<!-- 表格显示  -->
	<table id="dg" class="easyui-treegrid" title="菜单设置" 
			  animate="true"  url=${pageContext.request.contextPath}/menuinfo/listall.do collapsible="true"
			idField='id' treeField='menuName'  rownumbers="false" 
			fitColumns="true"  toolbar="#toolbar" state="closed" data-options="onContextMenu:onContextMenu">
		<thead>
			<tr>
				<th data-options="field:'menuName',width:'19%',align:'left' ,halign:'center'">菜单名称</th>
				<th data-options="field:'id',width:'15%',align:'center' ,halign:'center'" >id</th>
				<th data-options="field:'menuCode',align:'center' ,halign:'center',width:'15%'">菜单编码</th>
				<th data-options="field:'menuIcon',align:'center' ,halign:'center',width:'15%'">菜单图片</th>
				<th data-options="field:'script',align:'center' ,halign:'center',width:'22%'">菜单URL</th>
				<th data-options="field:'remark',align:'center' ,halign:'center',width:'15%'">菜单描述</th>
			</tr>
		</thead>
	</table>
	<!-- 表格上方的操作栏 -->
	<div id="toolbar">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newMenu(false)">新增菜单</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editMenu()">编辑菜单</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyMenu()">删除菜单</a>
    </div>
	<!-- 右键菜单 -->
	<div id="mm" class="easyui-menu" style="width:120px;">
		<div onclick="newMenu(true)" data-options="iconCls:'icon-add'">添加菜单</div>
		<div onclick="editMenu()" data-options="iconCls:'icon-edit'">编辑菜单</div>
		<div onclick="destroyMenu()" data-options="iconCls:'icon-remove'">删除菜单</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width:500px;height:400px;padding:10px 20px;"
	    		closed="true" buttons="#dlg-buttons" align="center" data-options="modal:true">
    	<form id="fm" method="post">
	    	<table cellpadding="5">
		    		<tr>
		    			<td><label>父菜单:</label></td>
		    			<td>
							 <select  id="cc" class="easyui-combotree" style="width:95%"
        		 				data-options="prompt:'父菜单'">
        					</select>
        					<input type="hidden" id="superMenu" name="superMenu">
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>菜单ID:</label></td>
		    			<td>
							 <input class="easyui-textbox easyui-validatebox" name="id" id="id" type="text" data-options="prompt:'菜单ID',required:true,missingMessage:'请添加菜单ID'"><span class="primary-tooltip"></span>
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>菜单名称:</label></td>
		    			<td>
							 <input class="easyui-textbox easyui-validatebox" type="text"  name="menuName" id="menuName" data-options="prompt:'菜单名称',required:true,missingMessage:'请添加菜单名称'">
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>菜单编码:</label></td>
		    			<td>
							 <input class="easyui-textbox easyui-validatebox"  type="text" name="menuCode" id="menuCode" data-options="prompt:'菜单编码',required:true,missingMessage:'请添加菜单编码'">
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>菜单url:</label></td>
		    			<td>
							 <input class="easyui-textbox easyui-validatebox" type="text" name="script" id="script" data-options="prompt:'菜单url',missingMessage:'请添加菜单url'">
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>菜单图片:</label></td>
		    			<td>
							<span class="l-btn-left l-btn-icon-left ">
								<span class="l-btn-text l-btn-empty">&nbsp;</span>
								<span class="l-btn-icon showIcon">&nbsp;</span>
							</span>
							<input type="hidden"  name="menuIcon" id="menuIcon">
							<a href="#" class="easyui-linkbutton"  onclick="browseImage()">浏览图片</a>
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>菜单描述:</label></td>
		    			<td><input class="easyui-textbox easyui-validatebox" name="remark" id="remark"  data-options="prompt: '菜单描述'"></td>
		    		</tr>
	    	</table>
    	</form>
    </div>
     <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveMenu()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div>
    <div id="brimg" class="easyui-dialog" style="width:300px;height:200px;padding:10px 20px;"
	    		closed="true"  align="center">
    	<form id="imgfm" method="post">
    		<a href="#" class="easyui-linkbutton" iconCls="icon-menu-sys" plain="true" onclick="getIcon(this)"></a>
    		<a href="#" class="easyui-linkbutton" iconCls="icon-menu-add" plain="true" onclick="getIcon(this)"></a>
    		<a href="#" class="easyui-linkbutton" iconCls="icon-menu-ok" plain="true" onclick="getIcon(this)"></a>
    		<a href="#" class="easyui-linkbutton" iconCls="icon-menu-users" plain="true" onclick="getIcon(this)"></a>
    		<a href="#" class="easyui-linkbutton" iconCls="icon-menu-nav" plain="true" onclick="getIcon(this)"></a>
    		<a href="#" class="easyui-linkbutton" iconCls="icon-menu-set" plain="true" onclick="getIcon(this)"></a>
    		<a href="#" class="easyui-linkbutton" iconCls="icon-menu-log" plain="true" onclick="getIcon(this)"></a>
    		<a href="#" class="easyui-linkbutton" iconCls="icon-menu-role" plain="true" onclick="getIcon(this)"></a>
    	</form>
    </div>
	<script type="text/javascript">
	//更改菜单选项图标
	function getIcon(e){
		//获取所选图标的类型
		var iconStr=$(e).attr('iconCls');
		//更新显示图标
		$(".showIcon").attr('class','l-btn-icon showIcon ');
		$(".showIcon").addClass(iconStr);
		//关闭浏览图片窗口
		$('#brimg').dialog('close');
		//去除 "-menu" 字符
		//由于菜单图标和普通图标有命名冲突，所以选择在编辑菜单图标的时候一普通图标的形式展示
		//这就需要解决命名问题，于是所浏览菜单图标一律使用"icon-menu-"前缀，普通图标是"icon-"前缀。
		//存到数据库的时候需要去掉"-menu"字符，因为菜单展示的时候是按"icon-"前缀的
		$("#menuIcon").val(iconStr.replace("-menu",""));
	}
	//右键菜单
/* 	var editingId;
	var selectedRow;
	var addFlag=false; */
	function onContextMenu(e,row){
		e.preventDefault();
		if(row==null){
			
		}else{
			$(this).treegrid('select', row.id);
		}
		$('#mm').menu('show',{
			left: e.pageX,
			top: e.pageY
		});
	}
	//
	function browseImage(){
		$('#brimg').dialog('open').dialog('setTitle','浏览图片');
	}
	var dialogType="";
	//顶部菜单
	 function newMenu(flag){
		  dialogType="new";
		  $('#dlg').dialog({
				onClose:function(){
					$('#id').textbox({
						onChange:$
					});
					$('.primary-tooltip').tooltip('hide');
				}
			});
		 $(".showIcon").attr('class','l-btn-icon showIcon ');
	    	$('#dlg').dialog('open').dialog('setTitle','添加菜单');
	    	
	    	var row = $('#dg').treegrid('getSelected');
	    	$('#fm').form('clear');
	    	$("#id").textbox({
	    		disabled : false
	    	});
	    	/* $("#script").textbox({
	    		disabled:true
	    	}); */
	    		url='${pageContext.request.contextPath}/menuinfo/add.do';
	    		$.ajax({
					url:'${pageContext.request.contextPath}/menuinfo/list.do',
					type:'get',
					success:function(result){
						$('#cc').combotree('loadData',result);
						$('#cc').combotree({
							editable:false
						});
					}
				});
	    		if(row&&flag){
		    		$('#cc').combotree('setValue', row.id);
	    		}
	    		$("#cc").combotree({
	    			onChange:function(n,o){
	    				if(n!=null){
	    					$("#script").textbox({
	    			    		disabled:false,
	    			    		required:true
	    			    	});
	    				}
	    			}
	    		});
	    		$('#id').textbox({
	    			onChange:primaryValidata
	    		});
	    	}
	 function editMenu(){
		 dialogType="edit";
		 $(".showIcon").attr('class','l-btn-icon showIcon ');
		 var row = $('#dg').treegrid('getSelected');
		 if(!row){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
	        if (row){
	        	$.ajax({
					url:'${pageContext.request.contextPath}/menuinfo/list.do',
					type:'get',
					success:function(result){
						$('#cc').combotree('loadData',result);
					}
				});
	        	if(row._parentId!=null){
	        		$('#cc').combotree('setValue', row._parentId);
	        		$("#script").textbox({
	    	    		disabled:false
	    	    	});
	        	}else{
	        		$('#cc').combotree('setValue', row.id);
	        		$("#script").textbox({
	    	    		disabled:true
	    	    	});
	        	}
	        	$('#dlg').dialog('open').dialog('setTitle','编辑菜单');
	        	$('#fm').form('load',row);
	        	$('#id').textbox('disable');
	        	//数据库中的字段
	        	var iconStr=$("#menuIcon").val();
	        	//截取字段-后的字符
	        	if(iconStr!=null&&iconStr!=""){
	        		var icon=iconStr.split("-");
		        	//需要展现的icon图标值
		        	var iconClass=icon[0]+"-menu-"+icon[1];
		        	$(".showIcon").addClass(iconClass);
	        	}
	        	url = '${pageContext.request.contextPath}/menuinfo/update.do?id='+row.id;
	        	}
	        }
	 function saveMenu(){
		 if(dialogType=="new"){
				if(!primaryValidata()){
					return false;
				}
			}
		 var t = $('#cc').combotree('tree');	// get the tree object
		 var n = t.tree('getSelected');		// get selected node
		if(n) $('#superMenu').val(n.id);
		 $.ajax({
			url:url,
			data:$('#fm').serialize(),
			type:'post',
			beforeSend: function(){
				var flag=$('#fm').form('validate');
				if(!flag){
					$.messager.show({
						title: '提示',
						msg:"请填写带*信息栏"
					});	
				}
				return flag;
			},
			success: function(result){
				if (result.success){
					$('#dg').treegrid('reload');	// reload the user data
					$('#dlg').dialog('close');		// close the dialog
					$.messager.show({
						title: '成功',
						msg:"操作成功"
					});		
					$('#dg').treegrid('reload');
				}else if (result.errorMsg){
					$.messager.show({
						title: '错误',
						msg: result.errorMsg
					});
				} 
			}
		 });
		 /* $('#fm').form('submit',{
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
     				$('#dg').treegrid('reload');	// reload the user data
     				$('#dlg').dialog('close');		// close the dialog
     			}
     		}
     	}); */
	 }
	   function destroyMenu(){
       	var row = $('#dg').treegrid('getSelected');
       	if(!row){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
       	if (row){
       		$.messager.confirm('删除','确认删除该菜单信息吗?',function(r){
       			if (r){
       				$.post('${pageContext.request.contextPath}/menuinfo/delete.do',{id:row.id},function(result){
       					if (result.success){
       						$('#dg').treegrid('reload');	// reload the user data
       						jsutil.msg.info("删除成功");
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
	 //主键校验提示框内容
		function primaryValidata(){
			var flag;
			var id=$('#id').textbox('getText');
			var params={}
			params.id="id";
			var url='${pageContext.request.contextPath}/menuinfo/find.do';
			flag=jsutil.primaryValidata(url, params)
			return flag;
		}
	</script>
</body>
</html>